package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingDtoResponse;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoResponse {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoResponse lastBooking;
    private BookingDtoResponse nextBooking;
    private List<CommentDtoResponse> comments;
    private long requestId;
}
