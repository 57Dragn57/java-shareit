package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.dto.Request;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RequestRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private RequestRepository requestRepository;

    private final User user = User.builder()
            .id(1L)
            .name("Masha")
            .email("mariya22@ya.ru")
            .build();

    @Test
    void findByRequestor() {
        entityManager.flush();
        List<Request> testList = requestRepository.findByRequestor(user);
        assertEquals(List.of(), testList);
    }

    @Test
    void findRequestsByRequestorIsNotOrderByCreatedDesc() {
        entityManager.flush();
        List<Request> testList = requestRepository.findRequestsByRequestorIsNotOrderByCreatedDesc(user, PageRequest.of(1, 1)).toList();
        assertEquals(List.of(), testList);
    }
}