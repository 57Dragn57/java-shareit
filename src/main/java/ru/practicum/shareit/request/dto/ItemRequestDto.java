package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Data
public class ItemRequestDto {
    int id;
    String description;
    User requestor;
    LocalDate created;
}
