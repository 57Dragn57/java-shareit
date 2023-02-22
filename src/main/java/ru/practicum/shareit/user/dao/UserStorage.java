package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserStorage {
    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, int userId);

    void delete(int id);

    UserDto getUser(int id);

    List<UserDto> getAllUsers();
}
