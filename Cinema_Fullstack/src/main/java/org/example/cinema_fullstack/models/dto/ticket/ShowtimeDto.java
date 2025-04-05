package org.example.cinema_fullstack.models.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowtimeDto {
    private Long showtimeId;
    private Long filmId;
    private String filmName;
    private String filmCategory;
    private String filmActors;
    private String filmDirectors;
    private Integer filmDuration;
    private String filmAge;
    private String filmImageUrl;
    private String filmTechnology;
    private String subtitle;
    private LocalDate showtimeDay;
    private LocalTime showtimeTime;
    private Long cinemaRoomId;
    private String cinemaRoomName;
}
