package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.dto.dto_cinemaroom.ListCinemaRoomDTO;
import org.example.cinema_fullstack.repositories.CinemaRoomRepository;
import org.example.cinema_fullstack.services.CinemaRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CinemaRoomServiceImpl implements CinemaRoomService {

    @Autowired
    private CinemaRoomRepository cinemaRoomRepository;

    @Override
    public Page<ListCinemaRoomDTO> listCinemaRoom(Pageable pageable) {
        return cinemaRoomRepository.listCinemaRoom(pageable);
    }

    @Override
    public Page<ListCinemaRoomDTO> searchByName(String name,Pageable pageable) {
        return cinemaRoomRepository.searchByName(name,pageable);
    }

    @Override
    public ListCinemaRoomDTO getCinemaRoomById(long id) {
        return cinemaRoomRepository.getCinemaRoomById(id);
    }

}
