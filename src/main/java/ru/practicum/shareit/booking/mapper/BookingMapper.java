package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking, UserStorage userStorage, ItemStorage itemStorage) {
        try {
            BookingDto b = BookingDto.builder()
                    .id(booking.getId())
                    .start(booking.getStart())
                    .end(booking.getEnd())
                    .itemId(booking.getItemId())
                    .item(ItemMapper.toItemDto(itemStorage.getItemById(booking.getItemId())))
                    .bookerId(booking.getBookerId())
                    .booker(UserMapper.toUserDto(userStorage.getUser(booking.getBookerId())))
                    .status(booking.getStatus())
                    .build();
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    public static Booking toBooking(BookingDto bookingDto) {
        Booking b = new Booking();
        b.setId(bookingDto.getId());
        b.setStart(bookingDto.getStart());
        b.setEnd(bookingDto.getEnd());
        b.setItemId(bookingDto.getItemId());
        b.setBookerId(bookingDto.getBookerId());
        b.setStatus(bookingDto.getStatus());

        return b;
    }

    public static List<BookingDto> bookingDtoList(List<Booking> bookings, UserStorage userStorage, ItemStorage itemStorage) {
        List<BookingDto> bdto = new ArrayList<>();
        for (Booking b : bookings) {
            bdto.add(toBookingDto(b, userStorage, itemStorage));
        }
        return bdto;
    }
}
