package org.example.cinema_fullstack.models.dto.ticket;

public interface BookingTicketDTO {
    String getCode();
    Long getId();
    Long getInvoiceId();
    Long getSeatId();
    String getSeatName();
    String getSeatCode();
    String getSeatType();
    Integer getPrice();
}
