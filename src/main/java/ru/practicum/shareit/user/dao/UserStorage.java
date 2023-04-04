package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(User user, long userId);

    void delete(long id);

    User getUser(long id);

    List<User> getAllUsers();

    boolean validation(long id);
}
