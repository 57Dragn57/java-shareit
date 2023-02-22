package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    public ItemDto addItem(ItemDto itemDto, int userId) {
        return itemStorage.addItem(itemDto, UserMapper.toUser(userService.getUser(userId)));
    }

    public ItemDto itemUpdate(ItemDto itemDto, int itemId, int userId) {
        return itemStorage.itemUpdate(itemDto, itemId, userId);
    }

    public ItemDto getItemById(int id) {
        return itemStorage.getItemById(id);
    }

    public List<ItemDto> getItemsByUser(int id) {
        return itemStorage.getItemsByUser(id);
    }

    public List<ItemDto> search(String text) {
        return itemStorage.search(text);
    }
}
