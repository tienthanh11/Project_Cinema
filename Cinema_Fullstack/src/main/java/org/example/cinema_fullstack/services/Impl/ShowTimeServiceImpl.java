package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.dto.showtime.CreateShowtimeDTO;
import org.example.cinema_fullstack.models.dto.ticket.*;
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
import java.util.ArrayList;
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
    public List<BookTicketShowtimeDto> getShowtimesByFilmId(Long filmId) {
        return showTimeRepository.getShowtimesByFilmId(filmId);
    }

    @Override
    public ShowtimeDto getShowtimeById(Long showtimeId) {
        List<BookTicketShowtimeDto> showtimes = showTimeRepository.getAllFilmShowingThisWeek();
        return showtimes.stream()
                .filter(s -> s.getShowtimeId().equals(showtimeId))
                .map(s -> new ShowtimeDto(
                        s.getShowtimeId(),
                        s.getFilmId(),
                        s.getFilmName(),
                        s.getFilmCategory(),
                        s.getFilmActors(),
                        s.getFilmDirectors(),
                        s.getFilmDuration(),
                        s.getFilmAge(),
                        s.getFilmImageUrl(),
                        s.getFilmTechnology(),
                        s.getSubtitle(),
                        s.getShowtimeDay(),
                        s.getShowtimeTime(),
                        s.getCinemaRoomId(),
                        s.getCinemaRoomName()
                ))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<SeatDto> getAllSeatByShowtimeId(Long showtimeId) {
        List<Object[]> results = showTimeRepository.getAllSeatByShowtimeId(showtimeId);
        List<SeatDto> seatList = new ArrayList<>();
        for (Object[] result : results) {
            SeatDto seat = new SeatDto(
                    result[0] != null ? ((Number) result[0]).longValue() : null, // seatId
                    (String) result[1], // seatName
                    (String) result[2], // seatType
                    (String) result[3], // seatCode
                    result[4] != null ? ((Number) result[4]).longValue() : null, // priceId
                    result[5] != null ? ((Number) result[5]).intValue() : null, // price
                    result[6] != null ? ((Number) result[6]).longValue() : null  // ticketId
            );
            seatList.add(seat);
        }
        return seatList;
    }

    @Override
    public CinemaRoomLayoutDto getCinemaRoomLayoutByShowtimeId(Long showtimeId) {
        CinemaRoomLayout layout = showTimeRepository.getCinemaRoomLayoutByShowtimeId(showtimeId);
        return new CinemaRoomLayoutDto(
                layout.getCinemaRoomId(),
                layout.getRowSeat(),
                layout.getColumnSeat(),
                layout.getSeatLayout()
        );
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
