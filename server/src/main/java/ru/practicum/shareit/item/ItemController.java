package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validated.Create;
import ru.practicum.shareit.validated.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDtoResponse addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @Validated(Create.class)
                                   @RequestBody ItemDtoRequest itemDtoRequest) {
        log.info("Добавление нового предмета");
        return itemService.addItem(itemDtoRequest, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoResponse createComment(@RequestHeader("X-Sharer-User-Id") long authorId,
                                            @PathVariable long itemId,
                                            @Valid @RequestBody CommentDtoRequest commentDtoRequest) {
        log.info("Добавление нового комментария");
        return itemService.createComment(commentDtoRequest, authorId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoResponse itemUpdate(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @Validated(Update.class)
                                      @RequestBody ItemDtoRequest itemDtoRequest,
                                      @PathVariable long itemId) {
        log.info("Обновление предмета");
        return itemService.itemUpdate(itemDtoRequest, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoResponse getItemById(@PathVariable long itemId,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Процесс получения предмета");
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoResponse> getItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                @Positive @RequestParam(defaultValue = "5") int size) {
        log.info("Пользователь просматривает свои предметы");
        return itemService.getItemsByUser(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDtoResponse> search(@RequestParam String text,
                                        @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                        @Positive @RequestParam(defaultValue = "5") int size) {
        log.info("Поиск предмета");
        if (text.isBlank()) {
            return List.of();
        }
        return itemService.search(text, from, size);
    }
}
