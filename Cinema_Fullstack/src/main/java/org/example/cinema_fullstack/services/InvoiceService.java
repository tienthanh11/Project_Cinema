package org.example.cinema_fullstack.services;

import org.example.cinema_fullstack.models.dto.ticket.BookTicketShowtimeDto;
import org.example.cinema_fullstack.models.dto.ticket.BookingInvoiceDTO;
import org.example.cinema_fullstack.models.dto.ticket.BookingTicketDTO;
import org.example.cinema_fullstack.models.entity.Invoice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceService {
    Invoice createInvoice(long membershipId, long paymentMethodId, String code);
    BookingInvoiceDTO getInvoiceByInvoiceId(long invoiceId);
//    void sendEmail(String email, String invoiceCode, BookTicketShowtimeDto showtime, List<BookingTicketDTO> ticketList);
    String generateCode();
}
