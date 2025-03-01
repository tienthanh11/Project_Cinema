package org.example.cinema_fullstack.models.dto.ticket;

public interface BookingSeatDTO {
    Long getId();
    String getName();
    String getSeatType();
    String getSeatCode();
    Long getPriceId();
    Integer getPrice();
    Long getTicketId();
}
