package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDto create(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Transactional
    public UserDto update(UserDto userDto, long userId) {
        User u = userRepository.findById(userId).orElseThrow(() -> new ValidationException("Пользователя не существует"));
        userDto.setId(userId);

        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            u.setEmail(userDto.getEmail());
        }

        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            u.setName(userDto.getName());
        }

        return UserMapper.toUserDto(u);
    }

    @Transactional
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    public UserDto getUser(long id) {
        return UserMapper.toUserDto(userRepository.findById(id).orElseThrow(() -> new ValidationException("Пользователя не существует")));
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public boolean validation(long id) {
        return userRepository.existsById(id);
    }
}
