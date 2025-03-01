package org.example.cinema_fullstack.models.dto.ticket;
import java.time.LocalDate;
import java.time.LocalTime;

public interface TicketDTO {
    Long getTicketId();
    String getFilmName();
    LocalTime getTime();
    LocalDate getDate();
    Double getPrice();
    Integer getStatus();
    String getSeatName();
}
