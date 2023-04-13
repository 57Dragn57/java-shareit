package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class RequestOnItemRequestDto {
    @NotBlank
    private String description;

    public RequestOnItemRequestDto(String description) {
        this.description = description;
    }

}
