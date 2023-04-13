package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    private final User owner = User.builder()
            .id(1L)
            .email("vasya@mail.ru")
            .name("Vasya")
            .build();
    private final User user = User.builder()
            .id(2L)
            .email("lena@mail.ru")
            .name("Elena")
            .build();

    private final UserDto userDto = UserDto.builder()
            .email("lena@mail.ru")
            .name("Elena")
            .build();

    private final Item item = Item.builder()
            .id(1L)
            .name("Дрель")
            .available(true)
            .description("Дрель обыкновенная")
            .owner(owner)
            .build();

    private final Booking booking = Booking.builder()
            .booker(user)
            .start(LocalDateTime.now().plusHours(1))
            .end(LocalDateTime.now().plusDays(1))
            .item(item)
            .status(BookingStatus.WAITING)
            .build();

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    BookingService bookingService;

    @Test
    void addBooking_validationException() {
        BookingDtoRequest bookingTest = BookingDtoRequest.builder()
                .itemId(0)
                .build();

        assertThrows(ValidationException.class, () -> bookingService.addBooking(bookingTest, 2L));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void addBooking() {
        lenient().when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        lenient().when(userService.getUser(anyLong())).thenReturn(userDto);

        assertEquals(any(), bookingRepository.save(booking));
        verify(bookingRepository).save(booking);
    }

    @Test
    void changeStatus_validationException() {
        long bookingId = 1L;
        long userId = 1L;
        boolean approved = true;
        assertThrows(ValidationException.class, () -> bookingService.changeStatus(bookingId, approved, userId));
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    void changeStatus() {
        assertEquals(any(), bookingRepository.save(booking));
        verify(bookingRepository).save(booking);
    }

    @Test
    void findBookingById_validationException() {
        long bookingId = 1L;
        long userId = 1L;
        assertThrows(ValidationException.class, () -> bookingService.findBookingById(bookingId, userId));
        verify(bookingRepository, never()).findById(bookingId);
    }

    @Test
    void findBookingById() {
        long bookingId = 1L;
        assertEquals(Optional.empty(), bookingRepository.findById(bookingId));
        verify(bookingRepository).findById(bookingId);
    }

    @Test
    void findBookingsListByBooker_validException() {
        String state = "ALL";
        long bookerId = 1L;
        int from = -1;
        int size = 5;
        assertThrows(ValidException.class, () -> bookingService.findBookingsListByBooker(state, bookerId, from, size));
    }

    @Test
    void findBookingsListByOwner() {
        String state = "ALL";
        long bookerId = 1L;
        int from = -1;
        int size = 5;
        assertThrows(ValidException.class, () -> bookingService.findBookingsListByOwner(state, bookerId, from, size));
    }

    @Test
    void findLastBooking() {
        long itemId = 1L;
        assertEquals(any(), bookingRepository.findLastBooking(itemId));
        verify(bookingRepository).findLastBooking(itemId);
    }

    @Test
    void findLastBookings() {
        assertEquals(List.of(), bookingService.findLastBookings(new ArrayList<>()));
    }

    @Test
    void findNextBooking() {
        long itemId = 1L;
        assertEquals(any(), bookingRepository.findNextBooking(itemId));
        verify(bookingRepository).findNextBooking(itemId);
    }

    @Test
    void findNextBookings() {
        assertEquals(List.of(), bookingService.findNextBookings(new ArrayList<>()));
    }

    @Test
    void findBookingByItemAndBooker() {
        long bookerId = 1L;
        long itemId = 0L;
        assertEquals(any(), bookingRepository.findBookingByBookerIdAndItemId(bookerId, anyLong()));
        verify(bookingRepository).findBookingByBookerIdAndItemId(bookerId, itemId);
    }
}