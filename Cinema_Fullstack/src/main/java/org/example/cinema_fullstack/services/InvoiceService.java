package org.example.cinema_fullstack.services;

import org.example.cinema_fullstack.common.SeatNotAvailableException;
import org.example.cinema_fullstack.models.dto.ticket.BookingInformation;
import org.example.cinema_fullstack.models.dto.ticket.BookingInvoiceDTO;
import org.example.cinema_fullstack.models.entity.Invoice;
import org.springframework.stereotype.Service;

@Service
public interface InvoiceService {

    void checkSeatAvailable(BookingInformation bookingInformation) throws SeatNotAvailableException;
    Invoice createInvoice(long membershipId, long paymentMethodId, String code);
    BookingInvoiceDTO getInvoiceByInvoiceId(long invoiceId);
//    void sendEmail(String email, String invoiceCode, BookTicketShowtimeDto showtime, List<BookingTicketDTO> ticketList);
    String generateCode();
}
