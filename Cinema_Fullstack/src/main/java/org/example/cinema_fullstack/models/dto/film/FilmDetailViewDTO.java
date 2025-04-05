package org.example.cinema_fullstack.models.dto.film;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FilmDetailViewDTO {
    private int id;
    private String name;
    private String imageURL;
    private LocalDate startDate;
    private LocalDate endDate;
    private String actors;
    private String studio;
    private int durations;
    private String directors;
    private String trailers;
    private String category;
    private String descriptions;
    private String age;
    private String filmTechnology;
    private String subTitle;

    public FilmDetailViewDTO(FilmViewDTO film) {
        this.id = film.getId();
        this.name = film.getName();
        this.imageURL = film.getImageURL();
        this.startDate = film.getStartDate();
        this.endDate = film.getEndDate();
        this.actors = film.getActors();
        this.studio = film.getStudio();
        this.durations = film.getDurations();
        this.directors = film.getDirectors();
        this.trailers = film.getTrailers();
        this.category = film.getCategory();
        this.descriptions = film.getDescriptions();
        this.filmTechnology = film.getFilmTechnology();
        this.subTitle = film.getSubTitle();

        if (film.getAge() != null) {
            if (film.getAge().equals("https://firebasestorage.googleapis.com/v0/b/a0720i1.appspot.com/o/dat-home%2Fc-16.png?alt=media&token=3ed7b6ef-9e9a-4f03-bece-3c0f281c1b63")) {
                this.age = "16 tuổi";
            } else {
                this.age = "18 tuổi";
            }
        } else {
            this.age = "Không xác định";
        }
    }
}