package org.example.cinema_fullstack.repositories;

import org.example.cinema_fullstack.models.entity.TicketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketPriceRepository extends JpaRepository<TicketPrice, Long> {
    List<TicketPrice> findAllBySeatCode(String seatCode);
}
