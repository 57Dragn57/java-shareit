package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;

    @Test
    void addBooking_whenBookingIsCorrect_thenStatusIsOk() throws Exception {
        BookingDtoRequest bookingDto = new BookingDtoRequest(
                0,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1),
                1L
        );
        long bookerId = 1L;

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", bookerId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(bookingService).addBooking(bookingDto, bookerId);
    }

    @Test
    void addBooking_whenBookingIsNotCorrect_thenStatusIsBad() throws Exception {
        BookingDtoRequest bookingDto = new BookingDtoRequest(
                0,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusDays(1),
                1L
        );
        long bookerId = 1L;

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", bookerId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        Mockito.verify(bookingService, Mockito.never()).addBooking(bookingDto, bookerId);
    }


    @Test
    void changeStatus_thenStatusIsOk() throws Exception {
        long bookingId = 1L;
        boolean approved = true;
        long userId = 1;

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(bookingService).changeStatus(bookingId, approved, userId);
    }

    @Test
    void findBookingById_thenStatusIsOk() throws Exception {
        long userId = 1L;
        long bookingId = 0L;

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(bookingService).findBookingById(bookingId, userId);
    }

    @Test
    void findBookingsByBookerState_thenStatusIsOk() throws Exception {
        String state = "ALL";
        long userId = 1L;
        int from = 0;
        int size = 5;

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "5"))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(bookingService).findBookingsListByBooker(state, userId, from, size);
    }

    @Test
    void findBookingsByBookerState_thenStatusBadRequest() throws Exception {
        String state = "ALL";
        long userId = 1L;
        int from = -1;
        int size = 5;

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("from", "-1")
                        .param("size", "5"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        Mockito.verify(bookingService, Mockito.never()).findBookingsListByBooker(state, userId, from, size);
    }

    @Test
    void findBookingsByOwnerState_thenStatusIsOk() throws Exception {
        String state = "ALL";
        long userId = 1L;
        int from = 0;
        int size = 5;

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "5"))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(bookingService).findBookingsListByOwner(state, userId, from, size);
    }

    @Test
    void findBookingsByOwnerState_thenStatusIsBadRequest() throws Exception {
        String state = "ALL";
        long userId = 1L;
        int from = 0;
        int size = -5;

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "-5"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        Mockito.verify(bookingService, Mockito.never()).findBookingsListByOwner(state, userId, from, size);
    }
}