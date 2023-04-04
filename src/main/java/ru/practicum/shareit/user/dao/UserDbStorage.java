package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Component
public class UserDbStorage implements UserStorage {
    private final UserRepository userRepository;

    public UserDbStorage(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user, long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ValidationException("Такого пользователя не существует");
        }

        User u = userRepository.getReferenceById(userId);
        user.setId(userId);

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            u.setEmail(user.getEmail());
        }

        if (user.getName() != null && !user.getName().isBlank()) {
            u.setName(user.getName());
        }

        return userRepository.save(u);
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUser(long id) {
        if (userRepository.existsById(id)) {
            return userRepository.getReferenceById(id);
        } else {
            throw new ValidationException("Пользователя не существует");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean validation(long id) {
        return userRepository.existsById(id);
    }
}
