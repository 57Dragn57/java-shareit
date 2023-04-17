package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoResponseTest {
    @Autowired
    private JacksonTester<ItemDtoResponse> json;

    @Test
    void itemResponseDtoTest() throws Exception {
        ItemDtoResponse itemTest = ItemDtoResponse.builder()
                .id(1L)
                .name("Otvertka")
                .description("asdasdasdasdasdsadas")
                .available(true)
                .lastBooking(new BookingDtoResponse())
                .nextBooking(new BookingDtoResponse())
                .requestId(3L)
                .comments(List.of())
                .build();

        JsonContent<ItemDtoResponse> result = json.write(itemTest);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Otvertka");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("asdasdasdasdasdsadas");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).hasJsonPath("$.lastBooking");
        assertThat(result).hasJsonPath("$.nextBooking");
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(3);
        assertThat(result).extractingJsonPathArrayValue("$.comments").isEqualTo(List.of());
    }
}