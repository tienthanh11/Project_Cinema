package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.entity.Seat;
import org.example.cinema_fullstack.repositories.SeatRepository;
import org.example.cinema_fullstack.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {
    @Autowired
    SeatRepository seatRepository;
    @Override
    public void updateTicketIdBySeatId(long ticketId, long seatId) {
        seatRepository.updateTicketIdBySeatId(ticketId, seatId);
    }

    @Override
    public Seat findById(long seatId) {
        return seatRepository.findById(seatId).orElse(null);
    }

    @Override
    public List<Seat> findBookedSeatsForShowtime(List<Long> seatIds, Long showtimeId) {
        return seatRepository.findBookedSeatsForShowtime(seatIds, showtimeId);
    }
}
