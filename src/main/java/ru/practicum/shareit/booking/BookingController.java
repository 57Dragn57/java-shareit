package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
@RequiredArgsConstructor
public class BookingController {
    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDtoResponse addBooking(@Valid @RequestBody BookingDtoRequest bookingDtoRequest,
                                         @RequestHeader("X-Sharer-User-Id") long bookerId) {
        log.info("Добавление нового бронирования");
        return bookingService.addBooking(bookingDtoRequest, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse changeStatus(@PathVariable long bookingId,
                                           @RequestParam boolean approved,
                                           @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Изменение статуса бронирования");
        return bookingService.changeStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse findBookingById(@PathVariable long bookingId,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получение бронирования по идентификатору");
        return bookingService.findBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoResponse> findBookingsByBookerState(@RequestParam(defaultValue = "ALL") String state,
                                                              @RequestHeader("X-Sharer-User-Id") long userId,
                                                              @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                              @Positive @RequestParam(defaultValue = "5") int size) {
        log.info("Получение списка бронирований со статусом {}", state);
        return bookingService.findBookingsListByBooker(state, userId, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> findBookingsByOwnerState(@RequestParam(defaultValue = "ALL") String state,
                                                             @RequestHeader("X-Sharer-User-Id") long userId,
                                                             @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                             @Positive @RequestParam(defaultValue = "5") int size) {
        log.info("Получение списка бронирований со статусом {}", state);
        return bookingService.findBookingsListByOwner(state, userId, from, size);
    }
}
