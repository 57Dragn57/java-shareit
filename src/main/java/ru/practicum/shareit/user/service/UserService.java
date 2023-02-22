package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public UserDto create(UserDto userDto) {
        return userStorage.create(userDto);
    }

    public UserDto update(UserDto userDto, int userId) {
        return userStorage.update(userDto, userId);
    }

    public void delete(int id) {
        userStorage.delete(id);
    }

    public UserDto getUser(int id) {
        return userStorage.getUser(id);
    }

    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers();
    }
}
