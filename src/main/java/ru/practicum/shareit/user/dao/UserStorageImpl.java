package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ServerErrorException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;

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
    public User create(User user) {
        mailValid(user);
        user.setId(id);
        userMap.put(id, user);
        id++;
        return user;
    }

    @Override
    public User update(User user, int userId) {
        User u = userMap.get(userId);
        user.setId(userId);

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            mailValid(user);
            u.setEmail(user.getEmail());
        }

        if (user.getName() != null && !user.getName().isBlank()) {
            u.setName(user.getName());
        }

        return u;
    }

    @Override
    public void delete(int id) {
        userMap.remove(id);
    }

    @Override
    public User getUser(int id) {
        if (userMap.containsKey(id)) {
            return userMap.get(id);
        } else {
            throw new ValidationException("Пользователя не существует");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    private void mailValid(User user) {
        for (User u : userMap.values()) {
            if (u.getEmail().equals(user.getEmail())) {
                if (u.getId() != user.getId()) {
                    throw new ServerErrorException("Пользователь с таким адресом эл.почты уже существует");
                }
            }
        }
    }
}

