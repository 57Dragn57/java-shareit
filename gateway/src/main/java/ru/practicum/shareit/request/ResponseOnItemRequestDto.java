package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.ItemDtoResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ResponseOnItemRequestDto {
    private long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDtoResponse> items;
}
