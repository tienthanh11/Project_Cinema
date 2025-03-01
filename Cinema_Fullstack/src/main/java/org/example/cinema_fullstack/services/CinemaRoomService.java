package org.example.cinema_fullstack.services;

import org.example.cinema_fullstack.models.dto.dto_cinemaroom.ListCinemaRoomDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CinemaRoomService {
    Page<ListCinemaRoomDTO> listCinemaRoom(Pageable pageable);
    Page<ListCinemaRoomDTO> searchByName(String name,Pageable pageable);

    ListCinemaRoomDTO getCinemaRoomById(long id);
}
