package org.example.cinema_fullstack.models.dto.showtime;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateShowtimeDTO {
    String filmTechnology;
    String subTitle;
    LocalDate day;
    LocalTime time;
    Long filmId;
    Long cinemaRoomId;

}
