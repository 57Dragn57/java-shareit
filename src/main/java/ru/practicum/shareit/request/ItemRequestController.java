package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestOnItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseOnItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private RequestService requestService;

    @Autowired
    public ItemRequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseOnItemRequestDto createRequest(@Valid @RequestBody RequestOnItemRequestDto requestDto, @RequestHeader("X-Sharer-User-Id") long requestorId) {
        log.info("Создание запроса");
        return requestService.createRequest(requestDto, requestorId);
    }

    @GetMapping
    public List<ResponseOnItemRequestDto> getRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получение своих запросов");
        return requestService.getRequests(userId);
    }

    @GetMapping("/all")
    public List<ResponseOnItemRequestDto> getRequestPage(@RequestHeader("X-Sharer-User-Id") long userId, @Positive @RequestParam(defaultValue = "0") int from, @Positive @RequestParam(defaultValue = "5") int size) {
        log.info("Получение страницы с запросами");
        return requestService.getRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseOnItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        log.info("Получение запроса по id");
        return requestService.getRequestById(requestId, userId);
    }
}
