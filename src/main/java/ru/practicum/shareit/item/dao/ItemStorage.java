package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemStorage {
    Item addItem(Item item, User user);

    Item itemUpdate(Item item, int itemId, int userId);

    Item getItemById(int id);

    List<Item> getItemsByUser(int id);

    List<Item> search(String text);
}
