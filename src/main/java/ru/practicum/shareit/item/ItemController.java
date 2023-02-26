package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validated.Create;
import ru.practicum.shareit.validated.Update;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") int userId, @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.info("Добавление нового предмета");
        return itemService.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto itemUpdate(@RequestHeader("X-Sharer-User-Id") int userId, @Validated(Update.class)
    @RequestBody ItemDto itemDto, @PathVariable int itemId) {
        log.info("Обновление предмета");
        return itemService.itemUpdate(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId) {
        log.info("Процесс получения предмета");
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemsByUser(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Пользователь просматривает свои предметы");
        return itemService.getItemsByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("Поиск предмета");
        if (text.isBlank()) {
            return List.of();
        }
        return itemService.search(text);
    }
}
