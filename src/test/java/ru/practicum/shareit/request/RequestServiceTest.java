package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.Request;
import ru.practicum.shareit.request.dto.RequestOnItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private ItemService itemService;
    @Mock
    private UserService userService;
    @InjectMocks
    private RequestService requestService;

    private final Request request = Request.builder()
            .created(LocalDateTime.now())
            .requestor(new User())
            .description("aaaaaaa bbbb")
            .build();

    @Test
    void createRequest() {
        long userId = 1L;

        assertEquals(any(), userService.getUser(userId));
        assertEquals(any(), requestRepository.save(request));

        verify(userService).getUser(userId);
        verify(requestRepository).save(request);
    }

    @Test
    void getRequests() {
        long requestId = 1L;

        assertEquals(List.of(), itemService.findItemByRequest(requestId));
        verify(itemService).findItemByRequest(requestId);
    }

    @Test
    void testGetRequests_throw() {
        long userId = 2L;
        int from = -1;
        int size = 1;

        assertThrows(ValidException.class, () -> requestService.getRequests(userId, from, size));
        verify(userService, never()).getUser(userId);
    }

    @Test
    void testGetRequests() {
        long requestId = 1L;
        long userId = 2L;

        assertEquals(any(), userService.getUser(userId));
        assertEquals(List.of(), itemService.findItemByRequest(requestId));

        verify(userService).getUser(userId);
        verify(itemService).findItemByRequest(requestId);
    }

    @Test
    void getRequestById_throw() {
        long requestId = 1L;
        long userId = 1L;

        assertThrows(ValidationException.class, () -> requestService.getRequestById(requestId, userId));
        verify(itemService, never()).findItemByRequest(requestId);
    }

    @Test
    void getRequestById() {
        lenient().when(userService.validation(anyLong())).thenReturn(false);
        lenient().when(itemService.findItemByRequest(anyLong())).thenReturn(List.of());
    }
}