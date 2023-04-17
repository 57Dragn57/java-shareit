package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ResponseOnItemRequestDtoTest {
    @Autowired
    private JacksonTester<ResponseOnItemRequestDto> json;

    @Test
    void requestDtoTest() throws Exception {
        ResponseOnItemRequestDto requestTest = ResponseOnItemRequestDto.builder()
                .id(5L)
                .created(LocalDateTime.of(2023, 4, 17, 1, 20))
                .items(List.of())
                .description("qwe123")
                .build();

        JsonContent<ResponseOnItemRequestDto> result = json.write(requestTest);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(5);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2023-04-17T01:20:00");
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(List.of());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("qwe123");
    }
}