package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validated.Create;
import ru.practicum.shareit.validated.Update;

@RestController
@Slf4j
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @PostMapping
    public ResponseEntity<Object> create(@Validated(Create.class) @RequestBody UserDto userDto) {
        return userClient.create(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@Validated(Update.class) @RequestBody UserDto userDto, @PathVariable long userId) {
        return userClient.update(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable long userId) {
        return userClient.delete(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable long userId) {
        return userClient.getUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }
}
