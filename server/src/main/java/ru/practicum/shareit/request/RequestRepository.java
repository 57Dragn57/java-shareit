package ru.practicum.shareit.request;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequestor(User user);

    Page<Request> findRequestsByRequestorIsNotOrderByCreatedDesc(User user, Pageable pageable);

}
