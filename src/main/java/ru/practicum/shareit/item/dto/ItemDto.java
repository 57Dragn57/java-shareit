package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
    int id;
    @NotEmpty
    String name;
    @NotEmpty
    @Size(max = 200)
    String description;
    @NotNull
    Boolean available;
}
