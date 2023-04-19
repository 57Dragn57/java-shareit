package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.ItemDtoResponse;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoResponse {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private long itemId;
    private ItemDtoResponse item;
    private long bookerId;
    private UserDto booker;
    private BookingStatus status;
}
