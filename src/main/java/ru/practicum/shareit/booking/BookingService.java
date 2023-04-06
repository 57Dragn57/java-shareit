package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    public BookingDtoResponse addBooking(BookingDtoRequest bookingDtoRequest, long bookerId) {
        if (userService.validation(bookerId) && itemRepository.existsById(bookingDtoRequest.getItemId())) {
            if (bookingDtoRequest.getItemId() == 0 || bookerId == 0) {
                throw new ValidationException("Переданы не все значения");
            }

            Booking booking = BookingMapper.toBooking(bookingDtoRequest);
            booking.setBooker(UserMapper.toUser(userService.getUser(bookerId)));
            booking.setItem(itemRepository.getReferenceById(bookingDtoRequest.getItemId()));
            booking.setStatus(BookingStatus.WAITING);

            if (booking.getItem().getOwner().getId() == bookerId) {
                throw new ValidationException("Вы не можете забронировать свой предмет");
            }

            if (booking.getItem().getAvailable() && booking.getStart().isBefore(booking.getEnd())) {
                return BookingMapper.toBookingDto(bookingRepository.save(booking));
            }
            throw new ValidException("Нет доступа для бронирования этого предмета");
        } else {
            throw new ValidationException("Пользователь не найден");
        }
    }

    public BookingDtoResponse changeStatus(long bookingId, boolean approved, long userId) {
        Booking b = findBooking(bookingId, userId);
        if (b.getItem().getOwner().getId() == userId) {

            if (!b.getStatus().equals(BookingStatus.WAITING)) {
                throw new ValidException("Повторное изменение статуса невозможна");
            }

            if (approved) {
                b.setStatus(BookingStatus.APPROVED);
            } else {
                b.setStatus(BookingStatus.REJECTED);
            }
            return BookingMapper.toBookingDto(bookingRepository.save(b));
        }
        throw new ValidationException("У вас нет доступа для изменения статуса");
    }

    public BookingDtoResponse findBookingById(long bookingId, long userId) {
        return BookingMapper.toBookingDto(findBooking(bookingId, userId));
    }

    public List<BookingDtoResponse> findBookingsListByBooker(String state, long bookerId) {
        if (userService.validation(bookerId)) {
            LocalDateTime now = LocalDateTime.now();

            try {
                BookingState s = BookingState.valueOf(state);

                switch (s) {
                    case ALL:
                        return BookingMapper.bookingDtoList(bookingRepository.findBookingsByBookerIdOrderByStartDesc(bookerId));
                    case CURRENT:
                        return BookingMapper.bookingDtoList(bookingRepository.findCurrentBookingsByBooker(bookerId, now, "APPROVED"));
                    case PAST:
                        return BookingMapper.bookingDtoList(bookingRepository.findPastBookingsByBooker(bookerId, now, "APPROVED"));
                    case FUTURE:
                        return BookingMapper.bookingDtoList(bookingRepository.findFutureBookingsByBooker(bookerId, now));
                    case WAITING:
                        return BookingMapper.bookingDtoList(bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING));
                    case REJECTED:
                        return BookingMapper.bookingDtoList(bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED));
                    default:
                        break;
                }
            } catch (Exception e) {
                throw new ValidException("Unknown state: " + state);
            }
        }
        throw new ValidationException("Пользователь не найден");
    }


    public List<BookingDtoResponse> findBookingsListByOwner(String state, long ownerId) {
        if (userService.validation(ownerId)) {
            LocalDateTime now = LocalDateTime.now();

            try {
                BookingState s = BookingState.valueOf(state);

                switch (s) {
                    case ALL:
                        return BookingMapper.bookingDtoList(bookingRepository.findAllBookingsByOwners(ownerId));
                    case CURRENT:
                        return BookingMapper.bookingDtoList(bookingRepository.findCurrentBookingsByOwner(ownerId, now, "APPROVED"));
                    case PAST:
                        return BookingMapper.bookingDtoList(bookingRepository.findPastBookingsByOwner(ownerId, now, "APPROVED"));
                    case FUTURE:
                        return BookingMapper.bookingDtoList(bookingRepository.findFutureBookingsByOwner(ownerId, now));
                    case WAITING:
                        return BookingMapper.bookingDtoList(bookingRepository.findOwnersBookingsByStatus(ownerId, "WAITING"));
                    case REJECTED:
                        return BookingMapper.bookingDtoList(bookingRepository.findOwnersBookingsByStatus(ownerId, "REJECTED"));
                    default:
                        throw new ValidException("Unknown state: " + state);
                }
            } catch (Exception e) {
                throw new ValidException("Unknown state: " + state);
            }
        }
        throw new ValidationException("Пользователь не найден");
    }

    public Booking findLastBooking(long itemId) {
        return bookingRepository.findLastBooking(itemId);
    }


    public Booking findNextBooking(long itemId) {
        return bookingRepository.findNextBooking(itemId);
    }


    public Booking findBookingByItemAndBooker(long bookerId, long itemId) {
        return bookingRepository.findBookingByBookerIdAndItemId(bookerId, itemId);
    }

    private Booking findBooking(long bookingId, long userId) {
        if (userService.validation(userId)) {
            if (bookingRepository.existsById(bookingId)) {
                Booking b = bookingRepository.getReferenceById(bookingId);
                Item item = b.getItem();
                if (userId == b.getBooker().getId() || userId == item.getOwner().getId()) {
                    return b;
                }
            }
            throw new ValidationException("Вы не являетесь владельцем вещи или создателем бронирования");
        } else {
            throw new ValidationException("Пользователь не найден");
        }
    }
}
