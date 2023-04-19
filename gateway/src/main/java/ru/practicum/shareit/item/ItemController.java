package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validated.Create;
import ru.practicum.shareit.validated.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Slf4j
@Validated
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @Validated(Create.class)
                                          @RequestBody ItemDtoRequest itemDtoRequest) {
        return itemClient.addItem(itemDtoRequest, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") long authorId,
                                                @PathVariable long itemId,
                                                @Valid @RequestBody CommentDtoRequest commentDtoRequest) {
        return itemClient.createComment(commentDtoRequest, authorId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> itemUpdate(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @Validated(Update.class)
                                             @RequestBody ItemDtoRequest itemDtoRequest,
                                             @PathVariable long itemId) {
        return itemClient.itemUpdate(itemDtoRequest, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable long itemId,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                 @Positive @RequestParam(defaultValue = "5") int size) {
        return itemClient.getItemsByUser(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                         @Positive @RequestParam(defaultValue = "5") int size) {
        return itemClient.search(text, from, size);
    }
}
