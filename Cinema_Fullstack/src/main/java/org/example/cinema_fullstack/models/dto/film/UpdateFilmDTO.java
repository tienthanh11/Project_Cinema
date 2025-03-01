package org.example.cinema_fullstack.models.dto.film;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateFilmDTO {
    long id;
    String name;
    String imageURL;
    LocalDate startDate;
    LocalDate endDate;
    String actors;
    String studio;
    int durations;
    String directors;
    String trailers;
    String category;
    String descriptions;
    String age;
}
