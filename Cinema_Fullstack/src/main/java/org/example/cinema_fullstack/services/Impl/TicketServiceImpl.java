package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.dto.ticket.BookingTicketDTO;
import org.example.cinema_fullstack.models.dto.ticket.TicketDTO;
import org.example.cinema_fullstack.models.dto.ticket.TicketMemberDTO;
import org.example.cinema_fullstack.models.entity.Invoice;
import org.example.cinema_fullstack.models.entity.Ticket;
import org.example.cinema_fullstack.repositories.InvoiceRepository;
import org.example.cinema_fullstack.repositories.TicketRepository;
import org.example.cinema_fullstack.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Override
    public long createTicket(long invoiceId) {
        Invoice invoice = invoiceRepository.getById(invoiceId);
        Ticket ticket = new Ticket();
        ticket.setInvoice(invoice);
        ticket = ticketRepository.save(ticket);
        return ticket.getId();
    }

    @Override
    public List<BookingTicketDTO> getAllTicketByInvoiceId(long invoiceId) {
        return ticketRepository.getAllTicketByInvoiceId(invoiceId);
    }

    @Override
    public Page<TicketDTO> findTicketOfMembership(Pageable pageable, Long memberId) {
        return ticketRepository.findTicketByMembership(pageable,memberId);

    }

    @Override
    public Page<TicketMemberDTO> findAllTicket(Pageable pageable) {
        return ticketRepository.findAllTicket(pageable);
    }

    @Override
    public void updatePrintedTicket(Long invoiceId) {
        ticketRepository.updateTicketPrinted(invoiceId);
    }

    @Override
    public Page<TicketMemberDTO> findAllTicketBySearch(String key, Pageable pageable) {
        return ticketRepository.findAllTicketBySearch(key,pageable);
    }

    @Override
    public TicketMemberDTO getInvoiceMember(Long id) {
        return ticketRepository.findInvoiceMemberById(id);
    }
}
