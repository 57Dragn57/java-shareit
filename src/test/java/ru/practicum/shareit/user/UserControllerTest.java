package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void create_whenUserIsCorrect_thenStatusIsOk() throws Exception {
        UserDto userDto = new UserDto(
                0,
                "Name",
                "name@ya.ru"
        );

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(userService).create(userDto);
    }

    @Test
    void create_whenUserIsNotCorrect_thenStatusIsBad() throws Exception {
        UserDto userDto = new UserDto(
                0,
                "Name",
                "name.Po4ta.ru"
        );

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        Mockito.verify(userService, Mockito.never()).create(userDto);
    }

    @Test
    void update_thenStatusIsOk() throws Exception {
        UserDto userDto = new UserDto(
                0,
                null,
                "name@yandex.ru"
        );
        long userId = 1L;

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(userService).update(userDto, userId);
    }

    @Test
    void delete_thenStatusIsOk() throws Exception {
        long userId = 1L;

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(userService).delete(userId);
    }

    @Test
    void getUser_thenStatusIsOk() throws Exception {
        long userId = 1L;

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(userService).getUser(userId);
    }

    @Test
    void getAllUsers_thenStatusIsOk() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(userService).getAllUsers();
    }
}