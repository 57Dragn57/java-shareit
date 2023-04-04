package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dao.BookingStorage;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;

    private final CommentRepository commentRepository;

    public ItemDto addItem(ItemDto itemDto, long userId) {
        if (userStorage.validation(userId)) {
            return ItemMapper.toItemDto(itemStorage.addItem(ItemMapper.toItem(itemDto), userId));
        }
        throw new ValidationException("Пользователя не существует");
    }

    public CommentDto createComment(CommentDto commentDto, long authorId, long itemId) {
        Booking b = bookingStorage.findBookingByItemAndBooker(authorId, itemId);

        if (b != null) {
            if (b.getEnd().isBefore(LocalDateTime.now())) {
                return CommentMapper.toCommentDto(itemStorage.createComment(CommentMapper.toComment(commentDto), authorId, itemId), userStorage);
            }
            throw new ValidException("Пользователь не брал предмет в аренду");
        }
        throw new ValidationException("Пользователь не брал предмет в аренду");
    }

    public ItemDto itemUpdate(ItemDto itemDto, long itemId, long userId) {
        if (userStorage.validation(userId)) {
            return ItemMapper.toItemDto(itemStorage.itemUpdate(ItemMapper.toItem(itemDto), itemId, userId));
        }
        throw new ValidationException("Пользователя не существует");
    }

    public ItemDto getItemById(long id, long userId) {
        Item item = itemStorage.getItemById(id);
        ItemDto idto = ItemMapper.toItemDto(item);
        idto.setComments(CommentMapper.commentDtoList(commentRepository.findCommentsByItemId(id), userStorage, itemStorage));

        if (item.getOwner() == userId) {
            idto.setLastBooking(BookingMapper.toBookingDto(bookingStorage.findLastBooking(id), userStorage, itemStorage));
            idto.setNextBooking(BookingMapper.toBookingDto(bookingStorage.findNextBooking(id), userStorage, itemStorage));
        }

        return idto;
    }

    public List<ItemDto> getItemsByUser(long id) {
        return ItemMapper.toItemDtoList(itemStorage.getItemsByUser(id),
                bookingStorage, commentRepository, userStorage, itemStorage);
    }

    public List<ItemDto> search(String text) {
        return ItemMapper.itemDtoList(itemStorage.search(text));
    }
}
