package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.Request;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByOwnerId(long owner, Pageable pageable);

    @Query(value = "select * " +
            "from items " +
            "where requests = ?1 ",
            nativeQuery = true)
    List<Item> findItemsByRequests(long requestId);

    List<Item> findByRequestsIn(List<Request> requests, Sort sort);

    Page<Item> findItemsByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(String text, String text1, Pageable pageable);
}
