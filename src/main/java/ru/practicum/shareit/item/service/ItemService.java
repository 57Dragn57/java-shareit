package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    public ItemDto addItem(ItemDto itemDto, int userId) {
        return ItemMapper.toItemDto(itemStorage.addItem(ItemMapper.toItem(itemDto), UserMapper.toUser(userService.getUser(userId))));
    }

    public ItemDto itemUpdate(ItemDto itemDto, int itemId, int userId) {
        return ItemMapper.toItemDto(itemStorage.itemUpdate(ItemMapper.toItem(itemDto), itemId, userId));
    }

    public ItemDto getItemById(int id) {
        return ItemMapper.toItemDto(itemStorage.getItemById(id));
    }

    public List<ItemDto> getItemsByUser(int id) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item i : itemStorage.getItemsByUser(id)) {
            itemDtoList.add(ItemMapper.toItemDto(i));
        }
        return itemDtoList;
    }

    public List<ItemDto> search(String text) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item i : itemStorage.search(text)) {
            itemDtoList.add(ItemMapper.toItemDto(i));
        }
        return itemDtoList;
    }
}
