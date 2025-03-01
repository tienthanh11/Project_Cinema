package org.example.cinema_fullstack.models.dto.dto_cinemaroom;

public interface ListCinemaRoomDTO {
    long getId();
    String getName();
    Integer getRowSeat();
    Integer getColumnSeat();
    Integer getStatus();
    String getSeatLayout();
}
