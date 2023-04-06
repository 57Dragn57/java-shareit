package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    public ItemDtoResponse addItem(ItemDtoRequest itemDtoRequest, long userId) {
        if (userService.validation(userId)) {
            Item item = ItemMapper.toItem(itemDtoRequest);
            item.setOwner(UserMapper.toUser(userService.getUser(userId)));
            return ItemMapper.toItemDto(itemRepository.save(item));
        }
        throw new ValidationException("Пользователя не существует");
    }

    public CommentDtoResponse createComment(CommentDtoRequest commentDtoRequest, long authorId, long itemId) {
        Booking b = bookingService.findBookingByItemAndBooker(authorId, itemId);

        if (b != null) {
            if (b.getEnd().isBefore(LocalDateTime.now())) {
                Comment comment = CommentMapper.toComment(commentDtoRequest);
                comment.setAuthor(UserMapper.toUser(userService.getUser(authorId)));
                comment.setItem(itemRepository.getReferenceById(itemId));
                comment.setCreated(LocalDateTime.now());

                return CommentMapper.toCommentDto(commentRepository.save(comment));
            }
            throw new ValidException("Пользователь не брал предмет в аренду");
        }
        throw new ValidationException("Пользователь не брал предмет в аренду");
    }

    public ItemDtoResponse itemUpdate(ItemDtoRequest itemDtoRequest, long itemId, long userId) {
        if (userService.validation(userId)) {
            if (!itemRepository.existsById(itemId)) {
                throw new ValidationException("Такого предмета не существует");
            }

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
            return ItemMapper.toItemDto(itemRepository.save(i));
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
        List<ItemDtoResponse> items = ItemMapper.itemDtoList(itemRepository.findByOwnerId(id));

        for (ItemDtoResponse i : items) {
            i.setLastBooking(BookingMapper.toBookingDto(bookingService.findLastBooking(i.getId())));
            i.setNextBooking(BookingMapper.toBookingDto(bookingService.findNextBooking(i.getId())));
            i.setComments(CommentMapper.commentDtoList(commentRepository.findCommentsByItemId(i.getId())));
        }

        return items;
    }

    public List<ItemDtoResponse> search(String text) {
        return ItemMapper.itemDtoList(
                itemRepository.findItemsByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text));
    }
}
