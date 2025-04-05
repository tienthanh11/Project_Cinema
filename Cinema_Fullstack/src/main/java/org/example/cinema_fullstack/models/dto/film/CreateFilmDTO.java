package org.example.cinema_fullstack.models.dto.film;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class CreateFilmDTO {
    private String name;
    private MultipartFile imageFile;
    private LocalDate startDate;
    private LocalDate endDate;
    private String actors;
    private String studio;
    private int duration;
    private String directors;
    private String trailers;
    private String[] category = new String[0];
    private String description;
    private String age;

    public String getCategoryAsString() {
        if (category == null || category.length == 0) {
            return "";
        }
        return String.join(" - ", category);
    }
}
