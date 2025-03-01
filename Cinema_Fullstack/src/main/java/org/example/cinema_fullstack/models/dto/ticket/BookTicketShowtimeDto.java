package org.example.cinema_fullstack.models.dto.ticket;

import java.time.LocalDate;
import java.time.LocalTime;

public interface BookTicketShowtimeDto {
    Long getFilmId();
    String getFilmName();
    String getFilmCategory();
    String getFilmActors();
    String getFilmDirectors();
    Integer getFilmDuration();
    String getFilmAge();
    String getFilmImageUrl();
    Long getShowtimeId();
    String getFilmTechnology();
    String getSubtitle();
    LocalDate getShowtimeDay();
    LocalTime getShowtimeTime();
    Long getCinemaRoomId();
    String getCinemaRoomName();
}
