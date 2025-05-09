package org.example.cinema_fullstack.services;


import org.example.cinema_fullstack.models.dto.showtime.CreateShowtimeDTO;
import org.example.cinema_fullstack.models.dto.ticket.*;
import org.example.cinema_fullstack.models.entity.CinemaRoom;
import org.example.cinema_fullstack.models.entity.Film;
import org.example.cinema_fullstack.models.entity.Showtime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface ShowTimeService {
    void createShowtimeDTO(CreateShowtimeDTO createShowtimeDTO);

    List<Film> findAllFilm();

    List<CinemaRoom> findAllCinemaRoom();

    CinemaRoom findById(long id);

    Long getMaxByIdShowtime();

    Showtime findShowtimeById(long id);

    List<BookTicketShowtimeDto> getAllFilmShowingThisWeek();

    List<BookTicketShowtimeDto> getShowtimesByFilmId(Long filmId);

    ShowtimeDto getShowtimeById(Long showtimeId);

    List<SeatDto> getAllSeatByShowtimeId(Long showtimeId);

    CinemaRoomLayoutDto getCinemaRoomLayoutByShowtimeId(Long showtimeId);

    BookTicketShowtimeDto getShowtimeByInvoiceId(long invoiceId);

    void createSeat(String name, long showtimeId, long ticketPriceId);

    List<Film> getAllFilmAvailable();

    List<Showtime> checkShowtimeAvailable(long cinemaRoomId, LocalDate day, LocalTime time);
}



