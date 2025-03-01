package org.example.cinema_fullstack.models.dto.showtime;

import lombok.Data;

import java.util.List;
@Data
public class ShowTimeDataDTO {
    List<CreateShowtimeDTO> showtimeList;
    List<CreateShowtimeSeatDTO> seatList;
}
