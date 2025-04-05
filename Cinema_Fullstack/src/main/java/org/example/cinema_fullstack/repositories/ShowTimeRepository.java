package org.example.cinema_fullstack.repositories;

import org.example.cinema_fullstack.models.dto.ticket.BookTicketShowtimeDto;
import org.example.cinema_fullstack.models.dto.ticket.CinemaRoomLayout;
import org.example.cinema_fullstack.models.dto.ticket.SeatDto;
import org.example.cinema_fullstack.models.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ShowTimeRepository extends JpaRepository<Showtime, Long> {


    @Transactional
    @Modifying
    @Query(value = "insert into showtime(film_technology, sub_title, day , time,film_id ,cinema_room_id) values (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    void createShowtimeDTO(String filmTechnology,
                           String subTitle,
                           LocalDate day,
                           LocalTime time,
                           Long filmId,
                           Long cinemaRoomId);

    @Query(value = "select max(id) from showtime", nativeQuery = true)
    Long getMaxIdShowtime();

    //Don
    @Query(value = "SELECT film.id as filmId, film.name as filmName, film.category as filmCategory, film.duration as filmDuration,\n" +
            "film.directors as filmDirectors, film.actors as filmActors, film.age filmAge, film.imageurl as filmImageUrl,\n" +
            "showtime.Id as showtimeId, showtime.cinema_room_id as cinemaRoomId, cinema_room.name as cinemaRoomName, showtime.day as showtimeDay, showtime.time as showtimeTime,\n" +
            "showtime.film_technology as filmTechnology, showtime.sub_title as subtitle \n" +
            "FROM film RIGHT JOIN showtime ON film.id = showtime.film_id\n" +
            "LEFT JOIN cinema_room on cinema_room.id = showtime.cinema_room_id " +
            " WHERE showtime.day >= CURDATE()  AND showtime.day <= NOW() + INTERVAL 7 DAY ORDER BY film.start_date DESC, film.name, showtime.day, showtime.time", nativeQuery = true)
    List<BookTicketShowtimeDto> getAllFilmShowingThisWeek();

    @Query(value = "SELECT film.id as filmId, film.name as filmName, film.category as filmCategory, film.duration as filmDuration, " +
            "film.directors as filmDirectors, film.actors as filmActors, film.age as filmAge, film.imageurl as filmImageUrl, " +
            "showtime.id as showtimeId, showtime.cinema_room_id as cinemaRoomId, cinema_room.name as cinemaRoomName, " +
            "showtime.day as showtimeDay, showtime.time as showtimeTime, " +
            "showtime.film_technology as filmTechnology, showtime.sub_title as subtitle " +
            "FROM film JOIN showtime ON film.id = showtime.film_id " +
            "LEFT JOIN cinema_room on cinema_room.id = showtime.cinema_room_id " +
            "WHERE film.id = ?1 AND showtime.day >= CURDATE() " +
            "ORDER BY showtime.day, showtime.time",
            nativeQuery = true)
    List<BookTicketShowtimeDto> getShowtimesByFilmId(Long filmId);

//    @Query(value = "SELECT seat.id as seatId, seat.name as seatName, ticket_price.seat_type as seatType, ticket_price.seat_code as seatCode, " +
//            "ticket_price.id as priceId, ticket_price.price as price, ticket.id as ticketId\n" +
//            "FROM seat\n" +
//            "LEFT JOIN showtime ON showtime.id = seat.showtime_id\n" +
//            "LEFT JOIN ticket_price ON seat.ticket_price_id = ticket_price.id\n" +
//            "LEFT JOIN ticket ON seat.ticket_id = ticket.id\n" +
//            "WHERE showtime.id = ?1", nativeQuery = true)
//    List<Object[]> getAllSeatByShowtimeId(Long showtimeId);

    @Query(value = "SELECT seat.id as seatId, seat.name as seatName, ticket_price.seat_type as seatType, ticket_price.seat_code as seatCode, " +
            "ticket_price.id as priceId, ticket_price.price as price, ticket.id as ticketId\n" +
            "FROM seat\n" +
            "LEFT JOIN showtime ON showtime.id = seat.showtime_id\n" +
            "LEFT JOIN ticket_price ON seat.ticket_price_id = ticket_price.id\n" +
            "LEFT JOIN ticket ON seat.ticket_id = ticket.id\n" +
            "WHERE showtime.id = ?1", nativeQuery = true)
    List<Object[]> getAllSeatByShowtimeId(Long showtimeId);

    @Query(value = "SELECT cinema_room.id as cinemaRoomId, cinema_room.row_seat as rowSeat, cinema_room.column_seat as columnSeat, " +
            "cinema_room.seat_layout as seatLayout FROM showtime \n" +
            "LEFT JOIN cinema_room ON cinema_room.id = showtime.cinema_room_id\n" +
            "WHERE showtime.id = ?1 LIMIT 1", nativeQuery = true)
    CinemaRoomLayout getCinemaRoomLayoutByShowtimeId(Long showtimeId);

    @Query(value = "SELECT film.id as filmId, film.name as filmName, film.category as filmCategory, film.duration as filmDuration,\n" +
            "film.directors as filmDirectors, film.actors as filmActors, film.age filmAge, film.imageurl as filmImageUrl,\n" +
            "showtime.Id as showtimeId, showtime.cinema_room_id as cinemaRoomId, cinema_room.name as cinemaRoomName, showtime.day as showtimeDay, showtime.time as showtimeTime,\n" +
            "showtime.film_technology as filmTechnology, showtime.sub_title as subtitle \n" +
            "FROM ticket left join seat on ticket.id = seat.ticket_id " +
            "LEFT JOIN showtime on seat.showtime_id = showtime.id " +
            "left join film on showtime.film_id = film.id " +
            "left join cinema_room on showtime.cinema_room_id = cinema_room.id " +
            "where ticket.invoice_id = ?1 limit 1", nativeQuery = true)
    BookTicketShowtimeDto getShowtimeByInvoiceId(long invoiceId);

    @Modifying
    @Query(value = "insert into seat(name, showtime_id, ticket_price_id) values (?1, ?2, ?3)", nativeQuery = true)
    void createSeat(String name, long showtimeId, long ticketPriceId);

    @Query(value = "select * from showtime where showtime.cinema_room_id = ?1 and showtime.day = ?2 and showtime.time = ?3", nativeQuery = true)
    List<Showtime> checkShowtimeAvailable(long cinemaRoomId, LocalDate day, LocalTime time);

}

