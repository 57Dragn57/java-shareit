package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserStorage {
    User create(User userDto);

    User update(User user, int userId);

    void delete(int id);

    User getUser(int id);

    List<User> getAllUsers();
}
