package org.example.cinema_fullstack.services;

import org.example.cinema_fullstack.models.dto.film.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FilmService {
    Page<ListFilmDTO> getListFilm(Pageable pageable);
    FilmViewDTO getFilmDTOById(long id);
    void updateFilm(UpdateFilmDTO updateFilmDTO);
    void createFilm(CreateFilmDTO createFilmDTO) throws Exception;
    void deleteFilm(long id);
    Page<ListFilmDTO> searchByName(String name, Pageable pageable);
    Page<ListFilmDTO> searchByNameAndStartDate(String name, String startDate, Pageable pageable);
    List<FilmDTO> listUpComingFilmDTO();

    List<FilmDTO> listUpShowingFilmDTO();

    List<FilmDTO> searchUpComingFilmDTO(String name);

    List<FilmDTO> searchUpShowingFilmDTO(String name);

    FilmDetailDTO getFilmById(long id);

    List<TicketPriceDTO> listTicketPrice();

    List<FilmTopDTO> getTopFilm();
}
