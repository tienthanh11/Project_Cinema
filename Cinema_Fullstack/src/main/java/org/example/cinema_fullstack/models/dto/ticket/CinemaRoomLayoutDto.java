package org.example.cinema_fullstack.models.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CinemaRoomLayoutDto {
    private Long cinemaRoomId;
    private Integer rowSeat;
    private Integer columnSeat;
    private String seatLayout;
}
