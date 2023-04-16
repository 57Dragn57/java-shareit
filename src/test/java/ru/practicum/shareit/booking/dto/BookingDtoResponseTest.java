package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoResponseTest {
    @Autowired
    private JacksonTester<BookingDtoResponse> json;

    @Test
    void bookingResponseDtoTest() throws Exception {
        BookingDtoResponse bookingTest = BookingDtoResponse.builder()
                .id(1L)
                .booker(new UserDto())
                .bookerId(1L)
                .end(LocalDateTime.of(2023, 4, 17, 12, 30))
                .start(LocalDateTime.of(2023, 4, 16, 12, 30))
                .item(new ItemDtoResponse())
                .itemId(1L)
                .status(BookingStatus.APPROVED)
                .build();

        JsonContent<BookingDtoResponse> result = json.write(bookingTest);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).hasJsonPath("$.booker");
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-04-17T12:30:00");
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-04-16T12:30:00");
        assertThat(result).hasJsonPath("$.item");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
    }
}