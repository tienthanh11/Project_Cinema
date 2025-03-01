package org.example.cinema_fullstack.services;

import org.example.cinema_fullstack.models.dto.ticket.BookingTicketDTO;
import org.example.cinema_fullstack.models.dto.ticket.TicketDTO;
import org.example.cinema_fullstack.models.dto.ticket.TicketMemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TicketService {
    long createTicket(long invoiceId);
    List<BookingTicketDTO> getAllTicketByInvoiceId(long invoiceId);
    Page<TicketDTO> findTicketOfMembership(Pageable pageable, Long memberId);
    Page<TicketMemberDTO> findAllTicket(Pageable pageable);
    void updatePrintedTicket(Long invoiceId);
    Page<TicketMemberDTO> findAllTicketBySearch(String key,Pageable pageable);
    TicketMemberDTO getInvoiceMember(Long id);
}
