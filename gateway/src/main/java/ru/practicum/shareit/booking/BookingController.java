package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
@RequiredArgsConstructor
public class BookingController {
    private BookingClient bookingClient;

    @Autowired
    public BookingController(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    @PostMapping
    public ResponseEntity<Object> addBooking(@Valid @RequestBody BookingDtoRequest bookingDtoRequest,
                                             @RequestHeader("X-Sharer-User-Id") long bookerId) {
        return bookingClient.addBooking(bookingDtoRequest, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> changeStatus(@PathVariable long bookingId,
                                               @RequestParam boolean approved,
                                               @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingClient.changeStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBookingById(@PathVariable long bookingId,
                                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingClient.findBookingById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findBookingsByBookerState(@RequestParam(defaultValue = "ALL") String state,
                                                            @RequestHeader("X-Sharer-User-Id") long userId,
                                                            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                            @Positive @RequestParam(defaultValue = "5") int size) {
        BookingState validState = BookingState.from(state).orElseThrow(() -> new ValidException("Unknown state: " + state));
        return bookingClient.findBookingsListByBooker(validState.name(), userId, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findBookingsByOwnerState(@RequestParam(defaultValue = "ALL") String state,
                                                           @RequestHeader("X-Sharer-User-Id") long userId,
                                                           @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                           @Positive @RequestParam(defaultValue = "5") int size) {
        BookingState validState = BookingState.from(state).orElseThrow(() -> new ValidException("Unknown state: " + state));
        return bookingClient.findBookingsListByOwner(validState.name(), userId, from, size);
    }
}
