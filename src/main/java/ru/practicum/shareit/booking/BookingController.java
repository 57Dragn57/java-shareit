package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody BookingDto bookingDto,
                                 @RequestHeader("X-Sharer-User-Id") long bookerId) {
        log.info("Добавление нового бронирования");
        return bookingService.addBooking(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeStatus(@PathVariable long bookingId, @RequestParam boolean approved,
                                   @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Изменение статуса бронирования");
        return bookingService.changeStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBookingById(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получение бронирования по идентификатору");
        return bookingService.findBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> findBookingsByBookerState(@RequestParam(defaultValue = "ALL") String state,
                                                      @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получение списка бронирований со статусом {}", state);
        return bookingService.findBookingsListByBooker(state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> findBookingsByOwnerState(@RequestParam(defaultValue = "ALL") String state,
                                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получение списка бронирований со статусом {}", state);
        return bookingService.findBookingsListByOwner(state, userId);
    }
}
