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

    @Query(value = "select invoice.id as invoiceId,membership.member_code as memberCode,invoice.code as code,membership.name as memberName,membership.card as memberCard,membership.phone as memberPhone, film.name as filmName,showtime.day,showtime.time,ticket.printed,cinema_room.name as cinemaRoom,group_concat(seat.name) as seatName,sum(ticket_price.price) as price from `ticket`\n" +
            "left join invoice on ticket.invoice_id = invoice.id\n" +
            "left join membership on membership.id = invoice.membership_id\n" +
            "left join seat on seat.ticket_id = ticket.id\n" +
            "left join ticket_price on seat.ticket_price_id = ticket_price.id\n" +
            "left join showtime on seat.showtime_id = showtime.id\n" +
            "left join cinema_room on showtime.cinema_room_id = cinema_room.id\n" +
            "left join film on showtime.film_id = film.id\n" +
            "group by invoice.id",nativeQuery = true)
    Page<TicketMemberDTO> findAllTicket(Pageable pageable);
    @Query(value = "select invoice.id as invoiceId,invoice.code as code,membership.member_code as memberCode,membership.name as memberName,membership.card as memberCard,membership.phone as memberPhone, film.name as filmName,showtime.day,showtime.time,ticket.printed,cinema_room.name as cinemaRoom,group_concat(seat.name) as seatName,sum(ticket_price.price) as price from `ticket`\n" +
            "left join invoice on ticket.invoice_id = invoice.id\n" +
            "left join membership on membership.id = invoice.membership_id\n" +
            "left join seat on seat.ticket_id = ticket.id\n" +
            "left join ticket_price on seat.ticket_price_id = ticket_price.id\n" +
            "left join showtime on seat.showtime_id = showtime.id\n" +
            "left join cinema_room on showtime.cinema_room_id = cinema_room.id\n" +
            "left join film on showtime.film_id = film.id\n" +
//            "where concat(membership.member_code,film.name,membership.phone,membership.name,'') like %?1% \n" +
            "where invoice.code like %?1% \n" +
            "group by invoice.id",nativeQuery = true)
    Page<TicketMemberDTO> findAllTicketBySearch(String key,Pageable pageable);
    @Query(value = "select invoice.id as invoiceId,invoice.code as code,membership.member_code as memberCode,membership.name as memberName,membership.card as memberCard,membership.phone as memberPhone, film.name as filmName,showtime.day,showtime.time,ticket.printed,cinema_room.name as cinemaRoom,group_concat(seat.name) as seatName,sum(ticket_price.price) as price from `ticket`\n" +
            "            left join invoice on ticket.invoice_id = invoice.id\n" +
            "            left join membership on membership.id = invoice.membership_id\n" +
            "            left join seat on seat.ticket_id = ticket.id\n" +
            "            left join ticket_price on seat.ticket_price_id = ticket_price.id\n" +
            "            left join showtime on seat.showtime_id = showtime.id\n" +
            "            left join cinema_room on showtime.cinema_room_id = cinema_room.id\n" +
            "            left join film on showtime.film_id = film.id\n" +
            "            where invoice.id = ?1 limit 1"
             ,nativeQuery = true)
    TicketMemberDTO findInvoiceMemberById(Long id);
    @Transactional
    @Modifying
    @Query(value = "update ticket \n" +
            "left join invoice on ticket.invoice_id = invoice.id\n" +
            "set printed = 1\n" +
            "where invoice.id= ?1",nativeQuery = true)
    void updateTicketPrinted(Long id);
}
