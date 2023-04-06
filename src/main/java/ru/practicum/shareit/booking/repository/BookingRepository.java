package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByBookerIdOrderByStartDesc(long bookerId);

    List<Booking> findBookingsByBookerIdAndStatusOrderByStartDesc(long bookerId, BookingStatus status);

    @Query(value = "select * " +
            "from BOOKINGS " +
            "where BOOKER_ID = ?1 and ?2 < START_DATE " +
            "group by START_DATE, ID " +
            "order by START_DATE DESC ", nativeQuery = true)
    List<Booking> findFutureBookingsByBooker(long bookerId, LocalDateTime date);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from BOOKINGS as b " +
            "where b.BOOKER_ID = ?1 and b.START_DATE < ?2 and ?2 < b.END_DATE " +
            "group by b.START_DATE, b.ID " +
            "order by b.START_DATE DESC ", nativeQuery = true)
    List<Booking> findCurrentBookingsByBooker(long bookerId, LocalDateTime date, String status);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from BOOKINGS as b " +
            "where b.BOOKER_ID = ?1 and ?2 > b.END_DATE and b.STATUS = ?3 " +
            "group by b.START_DATE, b.ID " +
            "order by b.START_DATE DESC ", nativeQuery = true)
    List<Booking> findPastBookingsByBooker(long bookerId, LocalDateTime date, String status);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from BOOKINGS as b join ITEMS I on b.ITEM_ID = I.ID " +
            "join USERS U on I.OWNER = U.ID " +
            "where U.ID = ?1 and b.START_DATE < ?2 and ?2 < b.END_DATE " +
            "group by b.START_DATE, b.ID " +
            "order by b.START_DATE DESC ", nativeQuery = true)
    List<Booking> findCurrentBookingsByOwner(long ownerId, LocalDateTime date, String status);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from bookings as b join ITEMS I on b.ITEM_ID = I.ID " +
            "join USERS U on I.OWNER = U.ID " +
            "where U.ID = ?1 and b.START_DATE > ?2 " +
            "group by b.START_DATE, b.ID " +
            "order by b.START_DATE DESC ", nativeQuery = true)
    List<Booking> findFutureBookingsByOwner(long ownerId, LocalDateTime date);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from bookings as b join ITEMS I on b.ITEM_ID = I.ID " +
            "join USERS U on I.OWNER = U.ID " +
            "where U.ID = ?1 and ?2 > b.END_DATE and STATUS = ?3 " +
            "group by b.START_DATE, b.ID " +
            "order by b.START_DATE DESC ", nativeQuery = true)
    List<Booking> findPastBookingsByOwner(long ownerId, LocalDateTime date, String status);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from bookings as b join ITEMS I on b.ITEM_ID = I.ID " +
            "join USERS U on I.OWNER = U.ID " +
            "where U.ID = ?1 " +
            "group by b.START_DATE, b.ID " +
            "order by b.START_DATE DESC ", nativeQuery = true)
    List<Booking> findAllBookingsByOwners(long ownerId);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from bookings as b join ITEMS I on b.ITEM_ID = I.ID " +
            "join USERS U on I.OWNER = U.ID " +
            "where U.ID = ?1 and  STATUS = ?2 " +
            "group by b.START_DATE, b.ID " +
            "order by b.START_DATE DESC ", nativeQuery = true)
    List<Booking> findOwnersBookingsByStatus(long ownerId, String status);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from bookings as b " +
            "where b.ITEM_ID = ?1 and b.START_DATE < now() and b.STATUS = 'APPROVED' " +
            "order by b.START_DATE DESC " +
            "limit 1", nativeQuery = true)
    Booking findLastBooking(long itemId);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from bookings as b " +
            "where b.ITEM_ID = ?1 and b.START_DATE > now() and b.STATUS = 'APPROVED' " +
            "order by b.START_DATE " +
            "limit 1", nativeQuery = true)
    Booking findNextBooking(long itemId);

    @Query(value = "select b.id, b.start_date, b.end_date, b.item_id, b.booker_id, b.status " +
            "from bookings as b " +
            "where b.BOOKER_ID = ?1 and b.ITEM_ID = ?2 " +
            "limit 1", nativeQuery = true)
    Booking findBookingByBookerIdAndItemId(long bookerId, long itemId);
}
