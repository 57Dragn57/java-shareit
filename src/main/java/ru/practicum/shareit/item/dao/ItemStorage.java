package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item addItem(Item item, long userId);

    Item itemUpdate(Item item, long itemId, long userId);

    Item getItemById(long id);

    List<Item> getItemsByUser(long id);

    List<Item> search(String text);

    Comment createComment(Comment comment, long authorId, long itemId);

}
