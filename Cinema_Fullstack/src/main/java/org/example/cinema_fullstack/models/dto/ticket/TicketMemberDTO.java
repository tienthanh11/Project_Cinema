package org.example.cinema_fullstack.models.dto.ticket;

import java.time.LocalDate;
import java.time.LocalTime;

public interface TicketMemberDTO {
    String getCode();
    Long getInvoiceId();
    String getMemberCode();
    String getMemberName();
    String getMemberCard();
    String getMemberPhone();
    String getFilmName();
    LocalDate getDay();
    LocalTime getTime();
    Long getPrinted();
    String getCinemaRoom();
    String getSeatName();
    Double getPrice();
}
