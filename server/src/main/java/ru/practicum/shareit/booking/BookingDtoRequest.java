package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingDtoRequest {
    private long id;
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;
    @NotNull
    @Future
    private LocalDateTime end;
    private long itemId;
}
