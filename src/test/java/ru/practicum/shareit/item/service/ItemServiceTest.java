package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDtoRequest;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    private final ItemDtoRequest itemDto = ItemDtoRequest.builder()
            .name("Тример электрический")
            .description("Для скоса травы")
            .available(true)
            .build();

    private final Item item = Item.builder()
            .name("Лопата")
            .description("Многофункциональная лопата")
            .owner(new User())
            .available(true)
            .build();

    private final CommentDtoRequest commentDto = CommentDtoRequest.builder()
            .text("Отличный аппарат")
            .build();

    private final Comment comment = Comment.builder()
            .created(LocalDateTime.now().minusHours(1))
            .item(item)
            .author(new User())
            .text("Просто огонь")
            .build();

    @Mock
    private UserService userService;
    @Mock
    private BookingService bookingService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private ItemService itemService;

    @Test
    void addItem_ValidationThrow() {
        assertThrows(ValidationException.class, () -> itemService.addItem(itemDto, anyLong()));
        verify(itemRepository, never()).save(item);
    }

    @Test
    void addItem() {
        long userId = 1L;
        assertEquals(any(), userService.getUser(userId));
        assertEquals(any(), itemRepository.save(item));

        verify(userService).getUser(userId);
        verify(itemRepository).save(item);
    }

    @Test
    void createComment_validThrow() {
        long authorId = 1L;
        long itemId = 1L;

        assertThrows(ValidException.class, () -> itemService.createComment(commentDto, authorId, itemId));
        verify(commentRepository, never()).save(comment);
    }

    @Test
    void createComment() {
        long authorId = 1L;
        long itemId = 1L;

        assertEquals(any(), itemRepository.getReferenceById(itemId));
        assertEquals(any(), commentRepository.save(comment));
        assertEquals(any(), userService.getUser(authorId));

        verify(itemRepository).getReferenceById(itemId);
        verify(commentRepository).save(comment);
        verify(userService).getUser(authorId);
    }

    @Test
    void itemUpdate_validationThrow() {
        long userId = 1L;
        long itemId = 1L;

        assertThrows(ValidationException.class, () -> itemService.itemUpdate(itemDto, itemId, userId));
        verify(itemRepository, never()).save(item);
    }

    @Test
    void itemUpdate() {
        long itemId = 1L;

        assertEquals(Optional.empty(), itemRepository.findById(itemId));
        verify(itemRepository).findById(itemId);
    }

    @Test
    void getItemById_throw() {
        long itemId = 1L;
        long userId = 1L;

        assertThrows(ValidationException.class, () -> itemService.getItemById(itemId, userId));
    }

    @Test
    void getItemById() {
        long itemId = 1L;

        assertEquals(Optional.empty(), itemRepository.findById(itemId));
        assertEquals(List.of(), commentRepository.findCommentsByItemId(itemId));
        assertEquals(any(), bookingService.findNextBooking(itemId));
        assertEquals(any(), bookingService.findLastBooking(itemId));

        verify(itemRepository).findById(itemId);
        verify(commentRepository).findCommentsByItemId(itemId);
        verify(bookingService).findNextBooking(itemId);
        verify(bookingService).findLastBooking(itemId);
    }

    @Test
    void getItemsByUser_throw() {
        long itemId = 1L;
        int from = -1;
        int size = 5;

        assertThrows(ValidException.class, () -> itemService.getItemsByUser(itemId, from, size));
        verify(itemRepository, never()).findByOwnerId(anyLong(), eq(PageRequest.of(from / size, size)));
    }

    @Test
    void getItemsByUser() {
        long userId = 1L;
        int from = 9;
        int size = 9;

        assertEquals(any(), itemRepository.findByOwnerId(userId, eq(PageRequest.of(from / size, size))));
    }

    @Test
    void search_throw() {
        String text = "sadsdasd";
        int from = -5;
        int size = 5;

        assertThrows(ValidException.class, () -> itemService.search(text, from, size));
    }

    @Test
    void findItemByRequest() {
        long requestId = 1L;
         assertEquals(List.of(), itemRepository.findItemsByRequests(requestId));
         verify(itemRepository).findItemsByRequests(requestId);
    }
}