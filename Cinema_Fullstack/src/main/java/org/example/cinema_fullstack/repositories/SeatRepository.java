package org.example.cinema_fullstack.repositories;

import org.example.cinema_fullstack.models.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Modifying
    @Query (value = "UPDATE seat SET seat.ticket_id = ?1 WHERE seat.id = ?2", nativeQuery = true)
    void updateTicketIdBySeatId(long ticketId, long seatId);

    @Query("SELECT s FROM Seat s WHERE s.id IN :seatIds AND s.showtime.id = :showtimeId AND s.ticket IS NOT NULL")
    List<Seat> findBookedSeatsForShowtime(List<Long> seatIds, Long showtimeId);
}
