package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.*;

@Component
@Slf4j
public class ItemStorageImpl implements ItemStorage {
    private final Map<Integer, Item> itemsMap = new HashMap<>();
    private final Map<Integer, List<Item>> userItemIndex = new LinkedHashMap<>();
    private int id = 1;

    @Override
    public Item addItem(Item item, User user) {
        item.setOwner(user);
        item.setId(id);
        itemsMap.put(id, item);
        id++;
        final List<Item> items = userItemIndex.computeIfAbsent(item.getOwner().getId(), k -> new ArrayList<>());
        items.add(item);
        return item;
    }

    @Override
    public Item itemUpdate(Item item, int itemId, int userId) {
        if (!itemsMap.containsKey(itemId)) {
            throw new ValidationException("Такого предмета не существует");
        }

        Item i = itemsMap.get(itemId);

        if (i.getOwner().getId() == userId) {

            if (item.getAvailable() != null) {
                i.setAvailable(item.getAvailable());
            }

            if (item.getDescription() != null && !item.getDescription().isBlank()) {
                i.setDescription(item.getDescription());
            }

            if (item.getName() != null && !item.getName().isBlank()) {
                i.setName(item.getName());
            }

            return i;
        } else {
            throw new ValidationException("Пользователь не является владельцем предмета");
        }
    }

    @Override
    public Item getItemById(int id) {
        if (itemsMap.containsKey(id)) {
            return itemsMap.get(id);
        } else {
            throw new ValidationException("Такого предмета не существует");
        }
    }

    @Override
    public List<Item> getItemsByUser(int id) {
        return userItemIndex.getOrDefault(id, List.of());
    }

    @Override
    public List<Item> search(String text) {
        List<Item> items = new ArrayList<>();

        for (Item i : itemsMap.values()) {
            if (i.getAvailable()) {
                if (i.getName().toLowerCase().contains(text.toLowerCase()) || i.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    items.add(i);
                }
            }
        }

        return items;
    }
}
