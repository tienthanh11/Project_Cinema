package org.example.cinema_fullstack.models.dto.film;

import java.time.LocalDate;

public interface ListFilmDTO {
    int getId();
    String getNameFilm();
    LocalDate getStart_date();
    String getStudio();
    int getDuration();
}
