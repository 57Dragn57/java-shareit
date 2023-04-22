package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class BookingMapper {
    public static BookingDtoResponse toBookingDto(Booking booking) {
        try {
            return BookingDtoResponse.builder()
                    .id(booking.getId())
                    .start(booking.getStart())
                    .end(booking.getEnd())
                    .itemId(booking.getItem().getId())
                    .item(ItemMapper.toItemDto(booking.getItem()))
                    .bookerId(booking.getBooker().getId())
                    .booker(UserMapper.toUserDto(booking.getBooker()))
                    .status(booking.getStatus())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    public static Booking toBooking(BookingDtoRequest bookingDtoRequest) {
        Booking b = new Booking();
        b.setId(bookingDtoRequest.getId());
        b.setStart(bookingDtoRequest.getStart());
        b.setEnd(bookingDtoRequest.getEnd());

        return b;
    }

    public static List<BookingDtoResponse> bookingDtoList(List<Booking> bookings) {
        List<BookingDtoResponse> bdto = new ArrayList<>();
        for (Booking b : bookings) {
            bdto.add(toBookingDto(b));
        }
        return bdto;
    }

}
