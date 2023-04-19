package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findBookingsByBookerIdOrderByStartDesc(long bookerId, Pageable pageable);

    Page<Booking> findBookingsByBookerIdAndStatusOrderByStartDesc(long bookerId, BookingStatus status, Pageable pageable);

    @Query(value = "select * " +
            "from BOOKINGS " +
            "where BOOKER_ID = ?1 and ?2 < START_DATE " +
            "group by START_DATE, ID " +
            "order by START_DATE DESC ", nativeQuery = true)
    Page<Booking> findFutureBookingsByBooker(long bookerId, LocalDateTime date, Pageable pageable);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from BOOKINGS as b " +
            "where b.BOOKER_ID = ?1 and b.START_DATE < ?2 and ?2 < b.END_DATE " +
            "group by b.START_DATE, b.ID " +
            "order by b.START_DATE DESC ", nativeQuery = true)
    Page<Booking> findCurrentBookingsByBooker(long bookerId, LocalDateTime date, String status, Pageable pageable);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from BOOKINGS as b " +
            "where b.BOOKER_ID = ?1 and ?2 > b.END_DATE and b.STATUS = ?3 " +
            "group by b.START_DATE, b.ID " +
            "order by b.START_DATE DESC ", nativeQuery = true)
    Page<Booking> findPastBookingsByBooker(long bookerId, LocalDateTime date, String status, Pageable pageable);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from BOOKINGS as b join ITEMS I on b.ITEM_ID = I.ID " +
            "join USERS U on I.OWNER = U.ID " +
            "where U.ID = ?1 and b.START_DATE < ?2 and ?2 < b.END_DATE " +
            "group by b.START_DATE, b.ID " +
            "order by b.START_DATE DESC ", nativeQuery = true)
    Page<Booking> findCurrentBookingsByOwner(long ownerId, LocalDateTime date, String status, Pageable pageable);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from bookings as b join ITEMS I on b.ITEM_ID = I.ID " +
            "join USERS U on I.OWNER = U.ID " +
            "where U.ID = ?1 and b.START_DATE > ?2 " +
            "group by b.START_DATE, b.ID " +
            "order by b.START_DATE DESC ", nativeQuery = true)
    Page<Booking> findFutureBookingsByOwner(long ownerId, LocalDateTime date, Pageable pageable);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from bookings as b join ITEMS I on b.ITEM_ID = I.ID " +
            "join USERS U on I.OWNER = U.ID " +
            "where U.ID = ?1 and ?2 > b.END_DATE and STATUS = ?3 " +
            "group by b.START_DATE, b.ID " +
            "order by b.START_DATE DESC ", nativeQuery = true)
    Page<Booking> findPastBookingsByOwner(long ownerId, LocalDateTime date, String status, Pageable pageable);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from bookings b join ITEMS I on b.ITEM_ID = I.ID " +
            "join USERS U on I.OWNER = U.ID " +
            "where U.ID = (:ownerId) " +
            "group by b.START_DATE, b.ID " +
            "order by b.START_DATE DESC ",
            countQuery = "SELECT count(*) FROM BOOKINGS "
            , nativeQuery = true)
    Page<Booking> findAllBookingsByOwners(@Param("ownerId") long ownerId, Pageable pageable);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from bookings as b join ITEMS I on b.ITEM_ID = I.ID " +
            "join USERS U on I.OWNER = U.ID " +
            "where U.ID = ?1 and STATUS = ?2 " +
            "group by b.START_DATE, b.ID " +
            "order by b.START_DATE DESC ", nativeQuery = true)
    Page<Booking> findOwnersBookingsByStatus(long ownerId, String status, Pageable pageable);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from bookings as b " +
            "where b.ITEM_ID = ?1 and b.START_DATE <= now() and b.STATUS = 'APPROVED' " +
            "order by b.START_DATE DESC " +
            "limit 1", nativeQuery = true)
    Booking findLastBooking(long itemId);

    List<Booking> findByItemInAndStartBeforeAndStatusOrderByStartDesc(List<Item> items, LocalDateTime dateTime, BookingStatus status);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from bookings as b " +
            "where b.ITEM_ID = ?1 and b.START_DATE > now() and b.STATUS = 'APPROVED' " +
            "order by b.START_DATE " +
            "limit 1", nativeQuery = true)
    Booking findNextBooking(long itemId);

    List<Booking> findByItemInAndStartAfterAndStatusOrderByStart(List<Item> items, LocalDateTime dateTime, BookingStatus status);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from bookings as b " +
            "where b.BOOKER_ID = ?1 and b.ITEM_ID = ?2 " +
            "limit 1", nativeQuery = true)
    Booking findBookingByBookerIdAndItemId(long bookerId, long itemId);
}
