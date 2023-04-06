package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto create(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    public UserDto update(UserDto userDto, long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ValidationException("Такого пользователя не существует");
        }

        User u = userRepository.getReferenceById(userId);
        userDto.setId(userId);

        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            u.setEmail(userDto.getEmail());
        }

        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            u.setName(userDto.getName());
        }

        return UserMapper.toUserDto(userRepository.save(u));
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }

    public UserDto getUser(long id) {
        if (userRepository.existsById(id)) {
            return UserMapper.toUserDto(userRepository.getReferenceById(id));
        } else {
            throw new ValidationException("Пользователя не существует");
        }
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public boolean validation(long id) {
        return userRepository.existsById(id);
    }
}
