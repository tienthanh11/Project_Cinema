package org.example.cinema_fullstack.models.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookTicketShowtimeDtoImpl implements BookTicketShowtimeDto {
    private Long filmId;
    private String filmName;
    private String filmCategory;
    private String filmActors;
    private String filmDirectors;
    private Integer filmDuration;
    private String filmAge;
    private String filmImageUrl;
    private Long showtimeId;
    private String filmTechnology;
    private String subtitle;
    private LocalDate showtimeDay;
    private LocalTime showtimeTime;
    private Long cinemaRoomId;
    private String cinemaRoomName;
}
