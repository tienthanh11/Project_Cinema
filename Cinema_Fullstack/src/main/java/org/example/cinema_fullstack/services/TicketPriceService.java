package org.example.cinema_fullstack.services;

import org.example.cinema_fullstack.models.entity.TicketPrice;
import org.springframework.stereotype.Service;

@Service
public interface TicketPriceService {
    TicketPrice getTicketPriceBySeatCode(String seatCode);
}
