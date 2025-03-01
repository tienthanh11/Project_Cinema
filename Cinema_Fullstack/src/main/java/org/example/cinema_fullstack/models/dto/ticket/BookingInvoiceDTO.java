package org.example.cinema_fullstack.models.dto.ticket;

public interface BookingInvoiceDTO {
    Long getId();
    String getCode();
    Long getMemberId();
    String getMemberCode();
    String getMemberName();
    String getMemberEmail();
    String getMemberPhone();
    String getPaymentMethod();
}
