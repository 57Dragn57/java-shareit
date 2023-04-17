package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(readOnly = true)
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Transactional
    public BookingDtoResponse addBooking(BookingDtoRequest bookingDtoRequest, long bookerId) {

        if (bookingDtoRequest.getItemId() == 0 || bookerId == 0) {
            throw new ValidationException("");
        }

        Booking booking = BookingMapper.toBooking(bookingDtoRequest);
        booking.setBooker(UserMapper.toUser(userService.getUser(bookerId)));
        booking.setItem(itemRepository.findById(bookingDtoRequest.getItemId()).orElseThrow(() -> new ValidationException("Предмет не существует")));
        booking.setStatus(BookingStatus.WAITING);

        if (booking.getItem().getOwner().getId() == bookerId) {
            throw new ValidationException("Владелец не может бронировать свой предмет");
        }

        if (booking.getItem().getAvailable() && booking.getStart().isBefore(booking.getEnd())) {
            return BookingMapper.toBookingDto(bookingRepository.save(booking));
        }
        throw new ValidException("Ошибка валидации доступа");
    }

    @Transactional
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

    public List<BookingDtoResponse> findBookingsListByBooker(String state, long bookerId, int from, int size) {
        if (userService.validation(bookerId)) {
            LocalDateTime now = LocalDateTime.now();

            try {
                BookingState s = BookingState.valueOf(state);

                switch (s) {
                    case ALL:
                        return BookingMapper.bookingDtoList(bookingRepository.findBookingsByBookerIdOrderByStartDesc(bookerId,
                                PageRequest.of(from / size, size)).toList());
                    case CURRENT:
                        return BookingMapper.bookingDtoList(bookingRepository.findCurrentBookingsByBooker(bookerId, now, BookingStatus.APPROVED.name(),
                                PageRequest.of(from / size, size)).toList());
                    case PAST:
                        return BookingMapper.bookingDtoList(bookingRepository.findPastBookingsByBooker(bookerId, now, BookingStatus.APPROVED.name(),
                                PageRequest.of(from / size, size)).toList());
                    case FUTURE:
                        return BookingMapper.bookingDtoList(bookingRepository.findFutureBookingsByBooker(bookerId, now,
                                PageRequest.of(from / size, size)).toList());
                    case WAITING:
                        return BookingMapper.bookingDtoList(bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(bookerId,
                                BookingStatus.WAITING, PageRequest.of(from / size, size)).toList());
                    case REJECTED:
                        return BookingMapper.bookingDtoList(bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(bookerId,
                                BookingStatus.REJECTED, PageRequest.of(from / size, size)).toList());
                    default:
                        break;
                }
            } catch (Exception e) {
                throw new ValidException("Unknown state: " + state);
            }
        }
        throw new ValidationException("Пользователь не найден");
    }


    public List<BookingDtoResponse> findBookingsListByOwner(String state, long ownerId, int from, int size) {
        if (userService.validation(ownerId)) {
            LocalDateTime now = LocalDateTime.now();

            try {
                BookingState s = BookingState.valueOf(state);

                switch (s) {
                    case ALL:
                        return BookingMapper.bookingDtoList(bookingRepository.findAllBookingsByOwners(ownerId,
                                PageRequest.of(from / size, size)).toList());
                    case CURRENT:
                        return BookingMapper.bookingDtoList(bookingRepository.findCurrentBookingsByOwner(ownerId, now, BookingStatus.APPROVED.name(),
                                PageRequest.of(from / size, size)).toList());
                    case PAST:
                        return BookingMapper.bookingDtoList(bookingRepository.findPastBookingsByOwner(ownerId, now, BookingStatus.APPROVED.name(),
                                PageRequest.of(from / size, size)).toList());
                    case FUTURE:
                        return BookingMapper.bookingDtoList(bookingRepository.findFutureBookingsByOwner(ownerId, now,
                                PageRequest.of(from / size, size)).toList());
                    case WAITING:
                        return BookingMapper.bookingDtoList(bookingRepository.findOwnersBookingsByStatus(ownerId, BookingStatus.WAITING.name(),
                                PageRequest.of(from / size, size)).toList());
                    case REJECTED:
                        return BookingMapper.bookingDtoList(bookingRepository.findOwnersBookingsByStatus(ownerId, BookingStatus.REJECTED.name(),
                                PageRequest.of(from / size, size)).toList());
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

    public List<Booking> findLastBookings(List<Item> items) {
        return bookingRepository.findByItemInAndStartBeforeAndStatusOrderByStartDesc(items, LocalDateTime.now(), BookingStatus.APPROVED);
    }

    public Booking findNextBooking(long itemId) {
        return bookingRepository.findNextBooking(itemId);
    }

    public List<Booking> findNextBookings(List<Item> items) {
        return bookingRepository.findByItemInAndStartAfterAndStatusOrderByStart(items, LocalDateTime.now(), BookingStatus.APPROVED);
    }

    public Booking findBookingByItemAndBooker(long bookerId, long itemId) {
        return bookingRepository.findBookingByBookerIdAndItemId(bookerId, itemId);
    }

    private Booking findBooking(long bookingId, long userId) {
        if (userService.validation(userId)) {
            Booking b = bookingRepository.findById(bookingId).orElseThrow(() -> new ValidationException("Вы не являетесь владельцем вещи или создателем бронирования"));
            Item item = b.getItem();
            if (userId == b.getBooker().getId() || userId == item.getOwner().getId()) {
                return b;
            }
            throw new ValidationException("Вы не являетесь владельцем вещи или создателем бронирования");
        } else {
            throw new ValidationException("Пользователь не найден");
        }
    }
}
