package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    void create() {
        lenient().when(userRepository.save(any())).thenReturn(new User());
    }

    @Test
    void update_throw() {
        long userId = 1L;
        assertThrows(ValidationException.class, () -> userService.update(new UserDto(), userId));
    }

    @Test
    void update() {
        long userId = 1L;
        lenient().when(userRepository.findById(userId)).thenReturn(Optional.empty());
    }

    @Test
    void delete() {
        long userId = 2L;
        userService.delete(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void getUser_throw() {
        long userId = 1L;
        assertThrows(ValidationException.class, () -> userService.getUser(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void getUser() {
        long userId = 1L;
        lenient().when(userRepository.findById(userId)).thenReturn(Optional.empty());
    }

    @Test
    void getAllUsers() {
        lenient().when(userRepository.findAll()).thenReturn(List.of());
        assertEquals(List.of(), userService.getAllUsers());
        verify(userRepository).findAll();
    }

    @Test
    void validation() {
        long userId = 1L;
        lenient().when(userRepository.existsById(userId)).thenReturn(false);
        assertFalse(userService.validation(userId));
        verify(userRepository).existsById(userId);
    }
}