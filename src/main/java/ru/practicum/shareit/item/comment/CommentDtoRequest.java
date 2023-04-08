package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CommentDtoRequest {
    private long id;
    @NotBlank
    private String text;
}
