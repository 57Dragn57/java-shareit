package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    long id;
    @NotBlank
    String text;
    String authorName;
    LocalDateTime created;
}
