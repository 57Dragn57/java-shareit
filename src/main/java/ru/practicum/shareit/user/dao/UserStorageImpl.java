package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ServerErrorException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserStorageImpl implements UserStorage {
    private final Map<Integer, User> userMap = new HashMap<>();
    private int id = 1;

    @Override
    public UserDto create(UserDto userDto) {
        mailValid(userDto);
        User user = UserMapper.toUser(userDto);
        user.setId(id);
        userMap.put(id, user);
        id++;
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(UserDto userDto, int userId) {
        userDto.setId(userId);
        mailValid(userDto);
        try {
            if (!userDto.getEmail().isBlank()) {
                userMap.get(userId).setEmail(userDto.getEmail());
            }
        } catch (Exception e) {
            log.info("Пустое значение электронной почты пользователя");
        }

        try {
            if (!userDto.getName().isBlank()) {
                userMap.get(userId).setName(userDto.getName());
            }
        } catch (Exception e) {
            log.info("Пустое значение имени пользователя");
        }

        return UserMapper.toUserDto(userMap.get(userId));
    }

    @Override
    public void delete(int id) {
        userMap.remove(id);
    }

    @Override
    public UserDto getUser(int id) {
        if (userMap.containsKey(id)) {
            return UserMapper.toUserDto(userMap.get(id));
        } else {
            throw new NotFoundException("Пользователя не существует");
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> dtoUsers = new ArrayList<>();
        for (User u : userMap.values()) {
            dtoUsers.add(UserMapper.toUserDto(u));
        }
        return dtoUsers;
    }

    private void mailValid(UserDto userDto) {
        for (User u : userMap.values()) {
            if (u.getEmail().equals(userDto.getEmail())) {
                if (u.getId() != userDto.getId()) {
                    throw new ServerErrorException("Пользователь с таким адресом эл.почты уже существует");
                }
            }
        }
    }
}

