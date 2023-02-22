package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ItemStorageImpl implements ItemStorage {
    private final Map<Integer, Item> itemsMap = new HashMap<>();
    private int id = 1;

    @Override
    public ItemDto addItem(ItemDto itemDto, User user) {
        @Valid Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        item.setId(id);
        itemsMap.put(id, item);
        id++;
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto itemUpdate(ItemDto itemDto, int itemId, int userId) {
        if (itemsMap.get(itemId).getOwner().getId() == userId) {
            try {
                if (itemDto.getAvailable() != null) {
                    itemsMap.get(itemId).setAvailable(itemDto.getAvailable());
                }
            } catch (Exception e) {
                log.info("Передано пустое значение поля доступа");
            }

            try {
                if (!itemDto.getDescription().isBlank()) {
                    itemsMap.get(itemId).setDescription(itemDto.getDescription());
                }
            } catch (Exception e) {
                log.info("Передано пустое значение поля с описанием");
            }

            try {
                if (!itemDto.getName().isBlank()) {
                    itemsMap.get(itemId).setName(itemDto.getName());
                }
            } catch (Exception e) {
                log.info("Передано пустое значение поля и именем");
            }

            return ItemMapper.toItemDto(itemsMap.get(itemId));
        } else {
            throw new NotFoundException("Пользователь не является владельцем предмета");
        }
    }

    @Override
    public ItemDto getItemById(int id) {
        if (itemsMap.containsKey(id)) {
            return ItemMapper.toItemDto(itemsMap.get(id));
        } else {
            throw new NotFoundException("Такого предмета не существует");
        }
    }

    @Override
    public List<ItemDto> getItemsByUser(int id) {
        List<ItemDto> userItems = new ArrayList<>();

        for (Item i : itemsMap.values()) {
            if (i.getOwner().getId() == id) {
                userItems.add(ItemMapper.toItemDto(i));
            }
        }

        return userItems;
    }

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> items = new ArrayList<>();

        if (text.isBlank()) {
            return items;
        }

        for (Item i : itemsMap.values()) {
            if (i.isAvailable()) {
                if (i.getName().toLowerCase().contains(text.toLowerCase()) || i.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    items.add(ItemMapper.toItemDto(i));
                }
            }
        }

        return items;
    }
}
