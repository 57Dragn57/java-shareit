package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoResponseTest {
    @Autowired
    private JacksonTester<CommentDtoResponse> json;

    @Test
    void commentResponseDtoTest() throws Exception {
        CommentDtoResponse commentTest = CommentDtoResponse.builder()
                .id(1L)
                .authorName("Nikita")
                .created(LocalDateTime.of(2023, 4, 17, 1, 3))
                .text("Abcde fghijkl mnop")
                .build();

        JsonContent<CommentDtoResponse> result = json.write(commentTest);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("Nikita");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2023-04-17T01:03:00");
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Abcde fghijkl mnop");
    }

}