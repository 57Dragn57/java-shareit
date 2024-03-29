package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ItemRepository itemRepository;

    private final Item item = Item.builder()
            .available(true)
            .description("asd23df")
            .name("Name")
            .build();

    @Test
    void findByOwnerId() {
        entityManager.flush();
        List<Item> testList = itemRepository.findByOwnerId(1L, PageRequest.of(1, 1)).toList();
        assertEquals(List.of(), testList);
    }

    @Test
    void findItemsByRequests() {
        entityManager.flush();
        List<Item> testList = itemRepository.findItemsByRequests(1L);
        assertEquals(List.of(), testList);
    }

    @Test
    void findItemsByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue() {
        entityManager.flush();
        List<Item> testList = itemRepository
                .findItemsByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue("text",
                        "text",
                        PageRequest.of(1, 1)).toList();
        assertEquals(List.of(), testList);
    }
}