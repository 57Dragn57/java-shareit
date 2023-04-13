package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByOwnerId(long owner, Pageable pageable);

    List<Item> findItemsByRequests(long requestId);

    Page<Item> findItemsByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(String text, String text1, Pageable pageable);
}
