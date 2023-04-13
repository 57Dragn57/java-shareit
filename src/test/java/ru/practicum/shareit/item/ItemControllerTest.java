package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.CommentDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.service.ItemService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;

    @Test
    void addItem_whenItemIsCorrect_thenStatusIsOk() throws Exception {
        ItemDtoRequest itemDto = new ItemDtoRequest(
                0,
                "Дрель",
                "Дрель обыкновенная",
                true,
                0
        );
        long userId = 1L;

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(itemService).addItem(itemDto, userId);
    }

    @Test
    void addItem_whenItemIsNotCorrect_thenStatusIsBad() throws Exception {
        ItemDtoRequest itemDto = new ItemDtoRequest(
                0,
                " ",
                "Дрель обыкновенная",
                true,
                0
        );
        long userId = 1L;

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        Mockito.verify(itemService, Mockito.never()).addItem(itemDto, userId);
    }

    @Test
    void createComment_whenCommentIsCorrect_thenStatusIsOk() throws Exception {
        CommentDtoRequest commentDto = new CommentDtoRequest(
                0,
                "Отличный аппарат"
        );
        long itemId = 1L;
        long authorId = 1L;

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", authorId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(itemService).createComment(commentDto, authorId, itemId);
    }

    @Test
    void createComment_whenCommentIsNotCorrect_thenStatusIsBad() throws Exception {
        CommentDtoRequest commentDto = new CommentDtoRequest(
                0,
                " "
        );
        long itemId = 1L;
        long authorId = 1L;

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", authorId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        Mockito.verify(itemService, Mockito.never()).createComment(commentDto, authorId, itemId);
    }

    @Test
    void itemUpdate_whenUpdateIsCorrect_thenStatusIsOk() throws Exception {
        ItemDtoRequest itemDto = new ItemDtoRequest(
                0,
                null,
                "Дрель необыкновенная",
                null,
                0
        );
        long itemId = 1L;
        long userId = 1L;

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(itemService).itemUpdate(itemDto, itemId, userId);
    }

    @Test
    void itemUpdate_whenUpdateIsNotCorrect_thenStatusIsBad() throws Exception {
        ItemDtoRequest itemDto = new ItemDtoRequest(
                0,
                null,
                "Когда берешь в руки инструмент, еще даже не работая, становится понятно, что от него ждать, " +
                        "если внешне сделан аккуратно, удобно и приятно держать в руках, " +
                        "нет шума и посторонних звуков в корпусе, то и в работе проявит себя хорошо. " +
                        "Так и вышло, по работе меня устраивает модель - отлично ставлю по всем параметрам, не подводила дрель.",
                null,
                0
        );
        long itemId = 1L;
        long userId = 1L;

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        Mockito.verify(itemService, Mockito.never()).itemUpdate(itemDto, itemId, userId);
    }

    @Test
    void getItemById_thenStatusIsOk() throws Exception {
        long itemId = 1L;
        long userId = 1L;

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(itemService).getItemById(itemId, userId);
    }

    @Test
    void getItemsByUser_thenStatusIsOk() throws Exception {
        long userId = 1L;
        int from = 0;
        int size = 5;

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "0")
                        .param("size", "5"))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(itemService).getItemsByUser(userId, from, size);
    }

    @Test
    void search_thenStatusIsOk() throws Exception {
        String text = "отвертка";
        int from = 0;
        int size = 5;

        mockMvc.perform(get("/items/search")
                        .param("text", text)
                        .param("from", "0")
                        .param("size", "5"))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(itemService).search(text, from, size);
    }
}