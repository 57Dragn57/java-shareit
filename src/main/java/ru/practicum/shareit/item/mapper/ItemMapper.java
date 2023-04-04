package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dao.BookingStorage;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
        return itemDto;
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        return item;
    }

    public static List<ItemDto> itemDtoList(List<Item> items) {
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public static List<ItemDto> toItemDtoList(List<Item> items, BookingStorage bookingStorage,
                                              CommentRepository commentRepository,
                                              UserStorage userStorage,
                                              ItemStorage itemStorage) {
        ItemDto itemDto;
        List<ItemDto> idto = new ArrayList<>();
        for (Item i : items) {
            itemDto = toItemDto(i);
            itemDto.setLastBooking(BookingMapper.toBookingDto(bookingStorage.findLastBooking(i.getId()), userStorage, itemStorage));
            itemDto.setNextBooking(BookingMapper.toBookingDto(bookingStorage.findNextBooking(i.getId()), userStorage, itemStorage));
            itemDto.setComments(CommentMapper.commentDtoList(commentRepository.findCommentsByItemId(i.getId()), userStorage, itemStorage));
            idto.add(itemDto);
        }
        return idto;
    }
}
