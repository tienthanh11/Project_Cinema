package org.example.cinema_fullstack.services;

import org.example.cinema_fullstack.models.entity.Seat;
import org.springframework.stereotype.Service;

@Service
public interface SeatService {
    void updateTicketIdBySeatId(long ticketId, long seatId);
    Seat findById(long seatId);
}
