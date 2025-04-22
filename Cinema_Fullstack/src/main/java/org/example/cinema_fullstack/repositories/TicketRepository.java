package org.example.cinema_fullstack.repositories;

import org.example.cinema_fullstack.models.dto.ticket.BookingTicketDTO;
import org.example.cinema_fullstack.models.dto.ticket.TicketDTO;
import org.example.cinema_fullstack.models.dto.ticket.TicketMemberDTO;
import org.example.cinema_fullstack.models.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query(value = "select ticket.id as id, ticket.invoice_id as invoiceId, seat.id as seatId, seat.name as seatName, " +
            "ticket_price.seat_code as seatCode, ticket_price.seat_type as seatType, ticket_price.price as price " +
            "from ticket left join seat on ticket.id = seat.ticket_id " +
            "left join ticket_price on seat.ticket_price_id =  ticket_price.id where ticket.invoice_id = ?1", nativeQuery = true)
    List<BookingTicketDTO> getAllTicketByInvoiceId(Long invoiceId);

//    @Query(value = "select invoice.id as invoiceId,ticket.id as ticketId,showtime.day as date,showtime.time as time,sum(ticket_price.price) as price,film.name as filmName,ticket.printed as status,group_concat(seat.name) as seatName from `ticket`\n" +
//            "left join invoice on ticket.invoice_id = invoice.id\n" +
//            "left join membership on membership.id = invoice.membership_id\n" +
//            "left join seat on seat.ticket_id = ticket.id\n" +
//            "left join ticket_price on seat.ticket_price_id = ticket_price.id\n" +
//            "left join showtime on seat.showtime_id = showtime.id\n" +
//            "left join film on showtime.film_id = film.id\n" +
//            "where membership.id=?1\n" +
//            "group by invoice.id",nativeQuery = true)
//    Page<TicketDTO> findTicketByMembership(Pageable pageable, Long id);


    @Query(value = "SELECT invoice.id AS invoiceId, ticket.id AS ticketId, " +
            "MAX(showtime.day) AS date, MAX(showtime.time) AS time, " +
            "SUM(ticket_price.price) AS price, MAX(film.name) AS filmName, " +
            "MAX(ticket.printed) AS status, GROUP_CONCAT(seat.name) AS seatName " +
            "FROM ticket " +
            "LEFT JOIN invoice ON ticket.invoice_id = invoice.id " +
            "LEFT JOIN membership ON membership.id = invoice.membership_id " +
            "LEFT JOIN seat ON seat.ticket_id = ticket.id " +
            "LEFT JOIN ticket_price ON seat.ticket_price_id = ticket_price.id " +
            "LEFT JOIN showtime ON seat.showtime_id = showtime.id " +
            "LEFT JOIN film ON showtime.film_id = film.id " +
            "WHERE membership.id = ?1 " +
            "GROUP BY invoice.id, ticket.id", nativeQuery = true)
    Page<TicketDTO> findTicketByMembership(Pageable pageable, Long id);

    @Query(value = "SELECT i.id AS invoiceId, i.code AS code, m.member_code AS memberCode, m.name AS memberName, " +
            "m.card AS memberCard, m.phone AS memberPhone, f.name AS filmName, s.day, s.time, t.printed, " +
            "cr.name AS cinemaRoom, agg.seatName, agg.price " +
            "FROM invoice i " +
            "LEFT JOIN membership m ON m.id = i.membership_id " +
            "LEFT JOIN ticket t ON t.invoice_id = i.id " +
            "LEFT JOIN showtime s ON s.id = (SELECT showtime_id FROM seat WHERE ticket_id = t.id LIMIT 1) " +
            "LEFT JOIN film f ON f.id = s.film_id " +
            "LEFT JOIN cinema_room cr ON cr.id = s.cinema_room_id " +
            "LEFT JOIN (" +
            "    SELECT t.invoice_id, GROUP_CONCAT(s.name) AS seatName, SUM(tp.price) AS price " +
            "    FROM ticket t " +
            "    LEFT JOIN seat s ON s.ticket_id = t.id " +
            "    LEFT JOIN ticket_price tp ON s.ticket_price_id = tp.id " +
            "    GROUP BY t.invoice_id" +
            ") agg ON agg.invoice_id = i.id " +
            "GROUP BY i.id, i.code, m.member_code, m.name, m.card, m.phone, f.name, s.day, s.time, t.printed, cr.name, agg.seatName, agg.price",
            nativeQuery = true)
    Page<TicketMemberDTO> findAllTicket(Pageable pageable);
    @Query(value = "SELECT i.id AS invoiceId, i.code AS code, m.member_code AS memberCode, m.name AS memberName, " +
            "m.card AS memberCard, m.phone AS memberPhone, f.name AS filmName, s.day, s.time, t.printed, " +
            "cr.name AS cinemaRoom, agg.seatName, agg.price " +
            "FROM invoice i " +
            "LEFT JOIN membership m ON m.id = i.membership_id " +
            "LEFT JOIN ticket t ON t.invoice_id = i.id " +
            "LEFT JOIN showtime s ON s.id = (SELECT showtime_id FROM seat WHERE ticket_id = t.id LIMIT 1) " +
            "LEFT JOIN film f ON f.id = s.film_id " +
            "LEFT JOIN cinema_room cr ON cr.id = s.cinema_room_id " +
            "LEFT JOIN (" +
            "    SELECT t.invoice_id, GROUP_CONCAT(s.name) AS seatName, SUM(tp.price) AS price " +
            "    FROM ticket t " +
            "    LEFT JOIN seat s ON s.ticket_id = t.id " +
            "    LEFT JOIN ticket_price tp ON s.ticket_price_id = tp.id " +
            "    GROUP BY t.invoice_id" +
            ") agg ON agg.invoice_id = i.id " +
            "WHERE i.code LIKE CONCAT('%', ?1, '%') " +
            "GROUP BY i.id, i.code, m.member_code, m.name, m.card, m.phone, f.name, s.day, s.time, t.printed, cr.name, agg.seatName, agg.price",
            nativeQuery = true)
    Page<TicketMemberDTO> findAllTicketBySearch(String key, Pageable pageable);
    @Query(value = "SELECT i.id AS invoiceId, i.code AS code, m.member_code AS memberCode, m.name AS memberName, " +
            "m.card AS memberCard, m.phone AS memberPhone, f.name AS filmName, s.day, s.time, t.printed, " +
            "cr.name AS cinemaRoom, " +
            "(SELECT GROUP_CONCAT(s2.name) FROM seat s2 WHERE s2.ticket_id = t.id) AS seatName, " +
            "(SELECT SUM(tp.price) FROM seat s3 LEFT JOIN ticket_price tp ON s3.ticket_price_id = tp.id WHERE s3.ticket_id = t.id) AS price " +
            "FROM invoice i " +
            "LEFT JOIN membership m ON m.id = i.membership_id " +
            "LEFT JOIN ticket t ON t.invoice_id = i.id " +
            "LEFT JOIN showtime s ON s.id = (SELECT showtime_id FROM seat WHERE ticket_id = t.id LIMIT 1) " +
            "LEFT JOIN film f ON f.id = s.film_id " +
            "LEFT JOIN cinema_room cr ON cr.id = s.cinema_room_id " +
            "WHERE i.id = ?1 " +
            "LIMIT 1",
            nativeQuery = true)
    TicketMemberDTO findInvoiceMemberById(Long id);
    @Transactional
    @Modifying
    @Query(value = "update ticket \n" +
            "left join invoice on ticket.invoice_id = invoice.id\n" +
            "set printed = 1\n" +
            "where invoice.id= ?1",nativeQuery = true)
    void updateTicketPrinted(Long id);
}
