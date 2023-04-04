package ru.practicum.shareit.booking.dao;

import ru.practicum.shareit.booking.Booking;

import java.util.List;

public interface BookingStorage {
    Booking addBooking(Booking booking, long bookerId);

    Booking changeStatus(long id, boolean approved, long userId);

    Booking findBookingById(long bookingId, long userId);

    List<Booking> findBookingsListByBooker(String state, long bookerId);

    List<Booking> findBookingsListByOwner(String state, long ownerId);

    Booking findLastBooking(long itemId);

    Booking findNextBooking(long itemId);

    Booking findBookingByItemAndBooker(long bookerId, long itemId);

}
