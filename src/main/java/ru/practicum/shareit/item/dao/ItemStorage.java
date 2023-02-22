package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemStorage {
    ItemDto addItem(ItemDto itemDto, User user);

    ItemDto itemUpdate(ItemDto itemDto, int itemId, int userId);

    ItemDto getItemById(int id);

    List<ItemDto> getItemsByUser(int id);

    List<ItemDto> search(String text);
}
