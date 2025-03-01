package org.example.cinema_fullstack.models.dto.film;

import java.time.LocalDate;

public interface FilmViewDTO {
    int getId();
    String getName();
    String getImageURL();
    LocalDate getStartDate();
    LocalDate getEndDate();
    String getActors();
    String getStudio();
    int getDurations();
    String getDirectors();
    String getTrailers();
    String getCategory();
    String getDescriptions();
    String getAge();
    String getFilmTechnology();
    String getSubTitle();
}
