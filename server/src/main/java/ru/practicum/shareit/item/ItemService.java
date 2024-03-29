package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.*;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemRepository itemRepository;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ItemDtoResponse addItem(ItemDtoRequest itemDtoRequest, long userId) {
        if (userService.validation(userId)) {
            Item item = ItemMapper.toItem(itemDtoRequest);
            item.setOwner(UserMapper.toUser(userService.getUser(userId)));

            if (itemDtoRequest.getRequestId() != 0) {
                item.setRequests(requestRepository.findById(itemDtoRequest.getRequestId())
                        .orElseThrow(() -> new ValidException("Запроса с таким id несуществует")));
            }

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

            Item i = itemRepository.findById(itemId).orElseThrow(() -> new ValidationException("Предмета не существует"));
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
        }
        throw new ValidationException("Пользователя не существует");
    }

    public ItemDtoResponse getItemById(long id, long userId) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ValidationException("Такого предмета не существует"));

        ItemDtoResponse idto = ItemMapper.toItemDto(item);
        idto.setComments(CommentMapper.commentDtoList(commentRepository.findCommentsByItemId(id)));

        if (item.getOwner().getId() == userId) {
            idto.setLastBooking(BookingMapper.toBookingDto(bookingService.findLastBooking(id)));
            idto.setNextBooking(BookingMapper.toBookingDto(bookingService.findNextBooking(id)));
        }
        return idto;
    }

    public List<ItemDtoResponse> getItemsByUser(long id, int from, int size) {
        List<Item> items = itemRepository.findByOwnerId(id, PageRequest.of(from / size, size, Sort.by(ASC, "id"))).toList();
        List<ItemDtoResponse> itemsDto = new ArrayList<>();

        Map<Item, List<Comment>> comments = commentRepository.findByItemIn(items, Sort.by(DESC, "created"))
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));

        Map<Item, Booking> lastBooking = bookingService.findLastBookings(items)
                .stream()
                .collect(toMap(Booking::getItem, identity(), (o, n) -> o));

        Map<Item, Booking> nextBooking = bookingService.findNextBookings(items)
                .stream()
                .collect(toMap(Booking::getItem, identity(), (o, n) -> o));

        for (Item item : items) {
            ItemDtoResponse itemDtoResponse = ItemMapper.toItemDto(item);

            if (comments.containsKey(item)) {
                itemDtoResponse.setComments(CommentMapper.commentDtoList(comments.get(item)));
            }

            if (lastBooking.containsKey(item)) {
                itemDtoResponse.setLastBooking(BookingMapper.toBookingDto(lastBooking.get(item)));
            }

            if (nextBooking.containsKey(item)) {
                itemDtoResponse.setNextBooking(BookingMapper.toBookingDto(nextBooking.get(item)));
            }
            itemsDto.add(itemDtoResponse);
        }

        return itemsDto;
    }

    public List<ItemDtoResponse> search(String text, int from, int size) {
        return ItemMapper.itemDtoList(
                itemRepository.findItemsByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text, PageRequest.of(from / size, size)).toList());
    }

    public List<ItemDtoResponse> findItemByRequest(long requestId) {
        return ItemMapper.itemDtoList(itemRepository.findItemsByRequests(requestId));
    }

    public Map<Request, List<Item>> findItemByRequest(List<Request> requests) {
        return itemRepository.findByRequestsIn(requests, Sort.by("id")).stream()
                .collect(groupingBy(Item::getRequests, toList()));
    }
}
