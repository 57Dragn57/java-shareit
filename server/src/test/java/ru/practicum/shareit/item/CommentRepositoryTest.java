package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CommentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CommentRepository commentRepository;
    private final Comment comment = Comment.builder()
            .text("asdasd fghj")
            .author(new User())
            .created(LocalDateTime.now())
            .item(new Item())
            .build();

    @Test
    void save() {
        entityManager.flush();

        Comment test = commentRepository.save(comment);

        assertEquals(1L, test.getId());
        assertEquals(comment.getCreated(), test.getCreated());
        assertEquals(comment.getText(), test.getText());
    }

    @Test
    void findCommentsByItemId() {
        entityManager.flush();
        List<Comment> testList = commentRepository.findCommentsByItemId(1L);
        assertEquals(List.of(), testList);
    }

    @Test
    void findByItemIn() {
        entityManager.flush();
        List<Comment> test = commentRepository.findByItemIn(List.of(), Sort.by("id"));
        assertEquals(List.of(), test);
    }
}