package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingDbStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.user.dao.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingDbStorage bookingDbStorage;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public BookingDto addBooking(BookingDto bookingDto, long bookerId) {
        if (userStorage.validation(bookerId)) {
            bookingDto.setBookerId(bookerId);
            return BookingMapper.toBookingDto(bookingDbStorage.addBooking(BookingMapper.toBooking(bookingDto), bookerId), userStorage, itemStorage);
        } else {
            throw new ValidationException("Пользователь не найден");
        }
    }

    public BookingDto changeStatus(long bookerId, boolean approved, long userId) {
        return BookingMapper.toBookingDto(bookingDbStorage.changeStatus(bookerId, approved, userId),
                userStorage, itemStorage);
    }

    public BookingDto findBookingById(long bookingId, long userId) {
        if (userStorage.validation(userId)) {
            return BookingMapper.toBookingDto(bookingDbStorage.findBookingById(bookingId, userId),
                    userStorage, itemStorage);
        } else {
            throw new ValidationException("Пользователь не найден");
        }
    }

    public List<BookingDto> findBookingsListByBooker(String state, long bookerId) {
        if (userStorage.validation(bookerId)) {
            return BookingMapper.bookingDtoList(bookingDbStorage.findBookingsListByBooker(state, bookerId),
                    userStorage, itemStorage);
        } else {
            throw new ValidationException("Пользователь не найден");
        }
    }

    public List<BookingDto> findBookingsListByOwner(String state, long ownerId) {
        if (userStorage.validation(ownerId)) {
            return BookingMapper.bookingDtoList(bookingDbStorage.findBookingsListByOwner(state, ownerId),
                    userStorage, itemStorage);
        } else {
            throw new ValidationException("Пользователь не найден");
        }
    }
}
