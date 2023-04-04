package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ItemDbStorage implements ItemStorage {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    public ItemDbStorage(ItemRepository itemRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Item addItem(Item item, long userId) {
        item.setOwner(userId);
        return itemRepository.save(item);
    }

    @Override
    public Comment createComment(Comment comment, long authorId, long itemId) {
        comment.setAuthorId(authorId);
        comment.setItemId(itemId);
        comment.setCreated(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public Item itemUpdate(Item item, long itemId, long userId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ValidationException("Такого предмета не существует");
        }

        Item i = itemRepository.getReferenceById(itemId);
        i.setId(itemId);

        if (i.getOwner() == userId) {

            if (item.getAvailable() != null) {
                i.setAvailable(item.getAvailable());
            }

            if (item.getDescription() != null && !item.getDescription().isBlank()) {
                i.setDescription(item.getDescription());
            }

            if (item.getName() != null && !item.getName().isBlank()) {
                i.setName(item.getName());
            }

            return itemRepository.save(i);
        } else {
            throw new ValidationException("Пользователь не является владельцем предмета");
        }
    }

    @Override
    public Item getItemById(long id) {
        if (itemRepository.existsById(id)) {
            return itemRepository.getReferenceById(id);
        } else {
            throw new ValidationException("Такого предмета не существует");
        }
    }

    @Override
    public List<Item> getItemsByUser(long id) {
        return itemRepository.findByOwner(id);
    }

    @Override
    public List<Item> search(String text) {
        return itemRepository.findItemsByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text);
    }
}
