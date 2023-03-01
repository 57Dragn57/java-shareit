package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public UserDto create(UserDto userDto) {
        return UserMapper.toUserDto(userStorage.create(UserMapper.toUser(userDto)));
    }

    public UserDto update(UserDto userDto, int userId) {
        return UserMapper.toUserDto(userStorage.update(UserMapper.toUser(userDto), userId));
    }

    public void delete(int id) {
        userStorage.delete(id);
    }

    public UserDto getUser(int id) {
        return UserMapper.toUserDto(userStorage.getUser(id));
    }

    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
