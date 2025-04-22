package org.example.cinema_fullstack.models.dto.showtime;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class ShowTimeDataDTO {
    private List<CreateShowtimeDTO> showtimeList = new ArrayList<>();
    private List<CreateShowtimeSeatDTO> seatList;
    private List<String> selectedTimes;
}
