package org.example.cinema_fullstack.models.dto.ticket;

public interface CinemaRoomLayout {
    Long getCinemaRoomId();
    Integer getRowSeat();
    Integer getColumnSeat();
    String getSeatLayout();
}
