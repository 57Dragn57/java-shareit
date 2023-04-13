package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.RequestOnItemRequestDto;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RequestService requestService;

    @Test
    void createRequest_whenRequestIsCorrect_thenStatusIsOk() throws Exception {
        RequestOnItemRequestDto requestDto = new RequestOnItemRequestDto(
                "Хотел бы воспользоваться щёткой для обуви"
        );
        long requestorId = 1L;

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", requestorId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(requestService).createRequest(requestDto, requestorId);
    }

    @Test
    void createRequest_whenRequestIsNotCorrect_thenStatusIsBad() throws Exception {
        RequestOnItemRequestDto requestDto = new RequestOnItemRequestDto(
                " "
        );
        long requestorId = 1L;

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", requestorId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        Mockito.verify(requestService, Mockito.never()).createRequest(requestDto, requestorId);
    }

    @Test
    void getRequests_thenStatusIsOk() throws Exception {
        long userId = 1L;

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(requestService).getRequests(userId);
    }

    @Test
    void getRequestPage_thenStatusIsOk() throws Exception {
        long userId = 1L;
        int from = 0;
        int size = 5;

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(requestService).getRequests(userId, from, size);
    }

    @Test
    void getRequestById_thenStatusIsOk() throws Exception {
        long userId = 1L;
        long requestId = 1L;

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(requestService).getRequestById(requestId, userId);
    }
}