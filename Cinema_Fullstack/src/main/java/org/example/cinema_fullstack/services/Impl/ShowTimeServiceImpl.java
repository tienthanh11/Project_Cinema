package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.dto.showtime.CreateShowtimeDTO;
import org.example.cinema_fullstack.models.dto.ticket.BookTicketShowtimeDto;
import org.example.cinema_fullstack.models.dto.ticket.BookingSeatDTO;
import org.example.cinema_fullstack.models.dto.ticket.CinemaRoomLayout;
import org.example.cinema_fullstack.models.entity.CinemaRoom;
import org.example.cinema_fullstack.models.entity.Film;
import org.example.cinema_fullstack.models.entity.Showtime;
import org.example.cinema_fullstack.repositories.CinemaRoomRepository;
import org.example.cinema_fullstack.repositories.FilmRepository;
import org.example.cinema_fullstack.repositories.ShowTimeRepository;
import org.example.cinema_fullstack.services.ShowTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ShowTimeServiceImpl implements ShowTimeService {

    @Autowired
    private ShowTimeRepository showTimeRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private CinemaRoomRepository cinemaRoomRepository;

    @Override
    public void createShowtimeDTO(CreateShowtimeDTO createShowtimeDTO) {
        showTimeRepository.createShowtimeDTO(createShowtimeDTO.getFilmTechnology(),
                                                createShowtimeDTO.getSubTitle(),
                                                createShowtimeDTO.getDay(),
                                                createShowtimeDTO.getTime(),
                                                createShowtimeDTO.getFilmId(),
                                                createShowtimeDTO.getCinemaRoomId());
    }

    @Override
    public List<Film> findAllFilm() {
        return filmRepository.findAll();
    }

    @Override
    public List<CinemaRoom> findAllCinemaRoom() {
        return cinemaRoomRepository.findAll();
    }

    @Override
    public Long getMaxByIdShowtime() {
        return showTimeRepository.getMaxIdShowtime();
    }

    @Override
    public Showtime getShowtimeById(long id) {
        return showTimeRepository.findById(id).orElse(null);
    }

    @Override
    public List<BookTicketShowtimeDto> getAllFilmShowingThisWeek() {
        return showTimeRepository.getAllFilmShowingThisWeek();
    }

    @Override
    public List<BookingSeatDTO> getAllSeatByShowtimeId(long showtimeId) {
        return showTimeRepository.getAllSeatByShowtimeId(showtimeId);
    }

    @Override
    public CinemaRoomLayout getCinemaRoomLayoutByShowtimeId(long showtimeId) {
        return showTimeRepository.getCinemaRoomLayoutByShowtimeId(showtimeId);
    }

    @Override
    public BookTicketShowtimeDto getShowtimeByInvoiceId(long invoiceId) {
        return showTimeRepository.getShowtimeByInvoiceId(invoiceId);
    }

    @Override
    public void createSeat(String name, long showtimeId, long ticketPriceId) {
        showTimeRepository.createSeat(name, showtimeId, ticketPriceId);
    }

    @Override
    public List<Film> getAllFilmAvailable() {
        return filmRepository.getAllFilmAvailable();
    }

    @Override
    public List<Showtime> checkShowtimeAvailable(long cinemaRoomId, LocalDate day, LocalTime time) {
        return showTimeRepository.checkShowtimeAvailable(cinemaRoomId, day, time);
    }


}
