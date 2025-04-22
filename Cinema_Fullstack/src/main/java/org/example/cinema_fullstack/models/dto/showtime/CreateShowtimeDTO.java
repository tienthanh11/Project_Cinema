package org.example.cinema_fullstack.models.dto.showtime;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateShowtimeDTO {
    private Long filmId;
    private LocalDate day;
    private Long cinemaRoomId;
    private String filmTechnology;
    private String subTitle;
    private LocalTime time;
}
