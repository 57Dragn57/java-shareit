package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ItemDtoResponse addItem(ItemDtoRequest itemDtoRequest, long userId) {
        if (userService.validation(userId)) {
            Item item = ItemMapper.toItem(itemDtoRequest);
            item.setOwner(UserMapper.toUser(userService.getUser(userId)));
            return ItemMapper.toItemDto(itemRepository.save(item));
        }
        throw new ValidationException("Пользователя не существует");
    }

    @Transactional
    public CommentDtoResponse createComment(CommentDtoRequest commentDtoRequest, long authorId, long itemId) {
        Booking b = bookingService.findBookingByItemAndBooker(authorId, itemId);

        if (b != null && b.getStatus().equals(BookingStatus.APPROVED)) {
            if (b.getEnd().isBefore(LocalDateTime.now())) {
                Comment comment = CommentMapper.toComment(commentDtoRequest);
                comment.setAuthor(UserMapper.toUser(userService.getUser(authorId)));
                comment.setItem(itemRepository.getReferenceById(itemId));
                comment.setCreated(LocalDateTime.now());

                return CommentMapper.toCommentDto(commentRepository.save(comment));
            }
        }
        throw new ValidException("Пользователь не брал предмет в аренду");
    }

    @Transactional
    public ItemDtoResponse itemUpdate(ItemDtoRequest itemDtoRequest, long itemId, long userId) {
        if (userService.validation(userId)) {

            try {
                Item i = itemRepository.getReferenceById(itemId);
                i.setId(itemId);

                if (i.getOwner().getId() == userId) {

                    if (itemDtoRequest.getAvailable() != null) {
                        i.setAvailable(itemDtoRequest.getAvailable());
                    }

                    if (itemDtoRequest.getDescription() != null && !itemDtoRequest.getDescription().isBlank()) {
                        i.setDescription(itemDtoRequest.getDescription());
                    }

                    if (itemDtoRequest.getName() != null && !itemDtoRequest.getName().isBlank()) {
                        i.setName(itemDtoRequest.getName());
                    }
                }
                return ItemMapper.toItemDto(i);
            } catch (Exception e) {
                throw new ValidationException("Такого предмета не существует");
            }
        }
        throw new ValidationException("Пользователя не существует");
    }

    public ItemDtoResponse getItemById(long id, long userId) {
        Item item;
        if (itemRepository.existsById(id)) {
            item = itemRepository.getReferenceById(id);

            ItemDtoResponse idto = ItemMapper.toItemDto(item);
            idto.setComments(CommentMapper.commentDtoList(commentRepository.findCommentsByItemId(id)));

            if (item.getOwner().getId() == userId) {
                idto.setLastBooking(BookingMapper.toBookingDto(bookingService.findLastBooking(id)));
                idto.setNextBooking(BookingMapper.toBookingDto(bookingService.findNextBooking(id)));
            }
            return idto;
        } else {
            throw new ValidationException("Такого предмета не существует");
        }
    }

    public List<ItemDtoResponse> getItemsByUser(long id) {
        List<Item> items = itemRepository.findByOwnerId(id);
        List<ItemDtoResponse> itemsDto = new ArrayList<>();

        Map<Item, List<Comment>> comments = commentRepository.findByItemIn(items, Sort.by(DESC, "created"))
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));

        Map<Item, List<Booking>> lastBooking = bookingService.findLastBookings(items)
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));

        Map<Item, List<Booking>> nextBooking = bookingService.findNextBookings(items)
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));

        for (Item i : items) {
            ItemDtoResponse idr = ItemMapper.toItemDto(i);

            if (comments.containsKey(i)) {
                idr.setComments(CommentMapper.commentDtoList(comments.get(i)));
            }

            if (lastBooking.containsKey(i)) {
                if (lastBooking.size() > 1) {
                    Booking last = lastBooking.get(i).get(0);
                    for (Booking b : lastBooking.get(i)) {
                        if (b.getStart().isAfter(last.getStart())) {
                            last = b;
                        }
                    }
                } else {
                    idr.setLastBooking(BookingMapper.toBookingDto(lastBooking.get(i).get(0)));
                }
            }

            if (nextBooking.containsKey(i)) {
                if (lastBooking.size() > 1) {
                    Booking next = nextBooking.get(i).get(0);
                    for (Booking b : nextBooking.get(i)) {
                        if (b.getStart().isBefore(next.getStart())) {
                            next = b;
                        }
                    }
                } else {
                    idr.setNextBooking(BookingMapper.toBookingDto(nextBooking.get(i).get(0)));
                }
            }
            itemsDto.add(idr);
        }

        return itemsDto;
    }

    public List<ItemDtoResponse> search(String text) {
        return ItemMapper.itemDtoList(
                itemRepository.findItemsByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text));
    }
}
