package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ResponseOnItemRequestDto {
    private long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
    private List<ItemDtoResponse> items;
}
