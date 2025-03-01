package org.example.cinema_fullstack.models.dto.ticket;

import lombok.Data;

import java.util.List;

@Data
public class BookingInformation {
    private Long showtimeId;
    private Long memberId;
    private List<Long> seatIdList;
    private BookTicketShowtimeDto showtime;
    private Long paymentMethodId;
}
