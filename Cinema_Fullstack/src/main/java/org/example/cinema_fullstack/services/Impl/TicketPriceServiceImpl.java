package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.entity.TicketPrice;
import org.example.cinema_fullstack.repositories.TicketPriceRepository;
import org.example.cinema_fullstack.services.TicketPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketPriceServiceImpl implements TicketPriceService {
    @Autowired
    TicketPriceRepository ticketPriceRepository;
    @Override
    public TicketPrice getTicketPriceBySeatCode(String seatCode) {
        return ticketPriceRepository.findAllBySeatCode(seatCode).get(0);
    }
}
