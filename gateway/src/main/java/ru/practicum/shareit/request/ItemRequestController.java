package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private ItemRequestClient requestClient;

    @Autowired
    public ItemRequestController(ItemRequestClient requestClient) {
        this.requestClient = requestClient;
    }

    @PostMapping
    public ResponseEntity<Object> createRequest(@Valid @RequestBody RequestOnItemRequestDto requestDto, @RequestHeader("X-Sharer-User-Id") long requestorId) {
        return requestClient.createRequest(requestDto, requestorId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestPage(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                 @Positive @RequestParam(defaultValue = "5") int size) {
        return requestClient.getRequestPage(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        return requestClient.getRequestById(requestId, userId);
    }
}
