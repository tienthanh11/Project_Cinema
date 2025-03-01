package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.dto.film.*;
import org.example.cinema_fullstack.repositories.FilmRepository;
import org.example.cinema_fullstack.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class FilmServiceImpl implements FilmService {
    @Autowired
    private FilmRepository filmRepository;

    @Override
    public Page<ListFilmDTO> getListFilm(Pageable pageable) {
        return filmRepository.getListFilm(pageable);
    }

    @Override
    public FilmViewDTO getFilmDTOById(long id) {
        return filmRepository.getFilmDTOById(id);
    }

    @Override
    public void updateFilm(UpdateFilmDTO updateFilmDTO) {
        this.filmRepository.updateFilm(
                updateFilmDTO.getId(),
                updateFilmDTO.getActors().replaceAll("\\s{2,}", " "),
                updateFilmDTO.getAge(),
                updateFilmDTO.getCategory(),
                updateFilmDTO.getDescriptions().replaceAll("\\s{2,}", " "),
                updateFilmDTO.getDirectors().replaceAll("\\s{2,}", " "),
                updateFilmDTO.getDurations(),
                updateFilmDTO.getImageURL(),
                updateFilmDTO.getName().replaceAll("\\s{2,}", " "),
                updateFilmDTO.getStudio(),
                updateFilmDTO.getStartDate(),
                updateFilmDTO.getEndDate(),
                updateFilmDTO.getTrailers());
    }

    @Override
    public void createFilm(CreateFilmDTO createFilmDTO) {
        filmRepository.createFilm(
                createFilmDTO.getActors().replaceAll("\\s{2,}", " "),
                createFilmDTO.getAge(),
                createFilmDTO.getCategory(),
                createFilmDTO.getDescription().replaceAll("\\s{2,}", " "),
                createFilmDTO.getDirectors().replaceAll("\\s{2,}", " "),
                createFilmDTO.getDuration(),
                createFilmDTO.getImageURL(),
                createFilmDTO.getName().replaceAll("\\s{2,}", " "),
                createFilmDTO.getStudio(),
                createFilmDTO.getStartDate(),
                createFilmDTO.getEndDate(),
                createFilmDTO.getTrailers());
    }

    @Override
    public void deleteFilm(long id) {
        this.filmRepository.deleteFilm(id);
    }

    @Override
    public Page<ListFilmDTO> searchByName(String name, Pageable pageable) {
        return this.filmRepository.searchByName(name,pageable);
    }

    @Override
    public Page<ListFilmDTO> searchByNameAndStartDate(String name, String startDate, Pageable pageable) {
        return this.filmRepository.searchByNameAndStartDate(name, startDate, pageable);
    }
    public List<FilmDTO> listUpComingFilmDTO() {
        return this.filmRepository.listUpComingFilmDTO();
    }

    @Override
    public List<FilmDTO> listUpShowingFilmDTO() {
        return this.filmRepository.listUpShowingFilmDTO();
    }

    @Override
    public List<FilmDTO> searchUpComingFilmDTO(String name) {
        return this.filmRepository.searchUpComingFilmDTO(name);
    }

    @Override
    public List<FilmDTO> searchUpShowingFilmDTO(String name) {
        return this.filmRepository.searchUpShowingFilmDTO(name);
    }

    @Override
    public FilmDetailDTO getFilmById(long id) {
        return this.filmRepository.getFilmById(id);
    }

    @Override
    public List<TicketPriceDTO> listTicketPrice() {
        return this.filmRepository.listTicketPrice();
    }

    @Override
    public List<FilmTopDTO> getTopFilm() {
        Random rand = new Random();
        List<FilmTopDTO> listFilm = this.filmRepository.getTopFilm();
        List<FilmTopDTO> newListFilm = new ArrayList<>();

        int index = Math.min(listFilm.size(), 3);

        for (int i = 0; i < index; i++) {
            int randomIndex = rand.nextInt(listFilm.size());
            newListFilm.add(listFilm.get(randomIndex));
            listFilm.remove(randomIndex);
        }
        return newListFilm;
    }
}



