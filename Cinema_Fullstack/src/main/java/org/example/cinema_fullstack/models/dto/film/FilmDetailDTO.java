package org.example.cinema_fullstack.models.dto.film;

import java.time.LocalDate;

public interface FilmDetailDTO {
    Long getId();
    String getActors();
    String getAge();
    String getCategory();
    String getDescription();
    String getDirectors();
    int getDuration();
    LocalDate getEnd_date();
    String getImageUrl();
    String getName();
    Double getRate();
    LocalDate getStart_date();
    String getStudio();
    String getTrailer();
}
