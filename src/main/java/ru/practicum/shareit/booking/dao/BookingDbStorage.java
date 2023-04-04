package ru.practicum.shareit.booking.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookingDbStorage implements BookingStorage {
    private final BookingRepository bookingRepository;
    private final ItemStorage itemStorage;

    public BookingDbStorage(BookingRepository bookingRepository, ItemStorage itemStorage) {
        this.bookingRepository = bookingRepository;
        this.itemStorage = itemStorage;
    }

    @Override
    public Booking addBooking(Booking booking, long bookerId) {
        booking.setBookerId(bookerId);
        booking.setStatus(BookingStatus.WAITING);

        if (itemStorage.getItemById(booking.getItemId()).getOwner() == bookerId) {
            throw new ValidationException("Вы не можете забранировать свой предмет");
        }

        if (itemStorage.getItemById(booking.getItemId()).getAvailable() &&
                booking.getStart().isBefore(booking.getEnd())) {
            return bookingRepository.save(booking);
        }
        throw new ValidException("Нет доступа для бронирования этого предмета");
    }

    @Override
    public Booking changeStatus(long bookingId, boolean approved, long userId) {
        Booking b = findBookingById(bookingId, userId);
        if (itemStorage.getItemById(b.getItemId()).getOwner() == userId) {

            if (!b.getStatus().equals(BookingStatus.WAITING)) {
                throw new ValidException("Повторное изменение статуса невозможна");
            }

            if (approved) {
                b.setStatus(BookingStatus.APPROVED);
            } else {
                b.setStatus(BookingStatus.REJECTED);
            }
            return bookingRepository.save(b);
        }
        throw new ValidationException("У вас нет доступа для изменения статуса");
    }

    @Override
    public Booking findBookingById(long bookingId, long userId) {
        if (bookingRepository.existsById(bookingId)) {
            Booking b = bookingRepository.getReferenceById(bookingId);
            Item item = itemStorage.getItemById(b.getItemId());
            if (userId == b.getBookerId() || userId == item.getOwner()) {
                return b;
            }
        }
        throw new ValidationException("Вы не являетесь владельцем вещи или создателем бронирования");
    }

    @Override
    public List<Booking> findBookingsListByBooker(String state, long bookerId) {
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case "ALL":
                return bookingRepository.findBookingsByBookerIdOrderByStartDesc(bookerId);
            case "CURRENT":
                return bookingRepository.findCurrentBookingsByBooker(bookerId, now, "APPROVED");
            case "PAST":
                return bookingRepository.findPastBookingsByBooker(bookerId, now, "APPROVED");
            case "FUTURE":
                return bookingRepository.findFutureBookingsByBooker(bookerId, now);
            case "WAITING":
                return bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING);
            case "REJECTED":
                return bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED);
            default:
                throw new ValidException("Unknown state: " + state);
        }
    }

    @Override
    public List<Booking> findBookingsListByOwner(String state, long ownerId) {
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case "ALL":
                return bookingRepository.findAllBookingsByOwners(ownerId);
            case "CURRENT":
                return bookingRepository.findCurrentBookingsByOwner(ownerId, now, "APPROVED");
            case "PAST":
                return bookingRepository.findPastBookingsByOwner(ownerId, now, "APPROVED");
            case "FUTURE":
                return bookingRepository.findFutureBookingsByOwner(ownerId, now);
            case "WAITING":
                return bookingRepository.findOwnersBookingsByStatus(ownerId, "WAITING");
            case "REJECTED":
                return bookingRepository.findOwnersBookingsByStatus(ownerId, "REJECTED");
            default:
                throw new ValidException("Unknown state: " + state);
        }
    }

    @Override
    public Booking findLastBooking(long itemId) {
        return bookingRepository.findLastBooking(itemId);
    }

    @Override
    public Booking findNextBooking(long itemId) {
        return bookingRepository.findNextBooking(itemId);
    }

    @Override
    public Booking findBookingByItemAndBooker(long bookerId, long itemId) {
        return bookingRepository.findBookingByBookerIdAndItemId(bookerId, itemId);
    }
}
