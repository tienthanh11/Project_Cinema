package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.dto.film.*;
import org.example.cinema_fullstack.models.entity.Film;
import org.example.cinema_fullstack.repositories.FilmRepository;
import org.example.cinema_fullstack.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class FilmServiceImpl implements FilmService {
    @Autowired
    private FilmRepository filmRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @Override
    public Page<ListFilmDTO> getListFilm(Pageable pageable) {
        return filmRepository.getListFilm(pageable);
    }

    @Override
    public FilmViewDTO getFilmDTOById(long id) {
        return filmRepository.getFilmDTOById(id);
    }

    @Override
    @Transactional
    public void updateFilm(UpdateFilmDTO updateFilmDTO) {
        String imageUrl = updateFilmDTO.getImageURL();
        if (updateFilmDTO.getImageFile() != null && !updateFilmDTO.getImageFile().isEmpty()) {
            try {
                String originalFileName = updateFilmDTO.getImageFile().getOriginalFilename();
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HHmmss"));
                String fileName = timestamp + "-" + originalFileName;

                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, updateFilmDTO.getImageFile().getBytes());

                imageUrl = "/uploads/" + fileName;
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image: " + e.getMessage());
            }
        }

        // Update the film in the database
        this.filmRepository.updateFilm(
                updateFilmDTO.getId(),
                updateFilmDTO.getActors().replaceAll("\\s{2,}", " "),
                updateFilmDTO.getAge(),
                updateFilmDTO.getCategoryAsString(),
                updateFilmDTO.getDescriptions().replaceAll("\\s{2,}", " "),
                updateFilmDTO.getDirectors().replaceAll("\\s{2,}", " "),
                updateFilmDTO.getDurations(),
                imageUrl,
                updateFilmDTO.getName().replaceAll("\\s{2,}", " "),
                updateFilmDTO.getStudio(),
                updateFilmDTO.getStartDate(),
                updateFilmDTO.getEndDate(),
                updateFilmDTO.getTrailers());
    }

    @Override
    public void createFilm(CreateFilmDTO createFilmDTO) throws Exception {
        String fileName = null;
        if (createFilmDTO.getImageFile() != null && !createFilmDTO.getImageFile().isEmpty()) {
            MultipartFile imageFile = createFilmDTO.getImageFile();
            String originalFileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HHmmss"));
            fileName = timestamp + "-" + originalFileName;

            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.write(path, imageFile.getBytes());
        }

        Film film = new Film();
        film.setName(createFilmDTO.getName().trim());
        film.setImageURL("/uploads/" + fileName);
        film.setStartDate(createFilmDTO.getStartDate());
        film.setEndDate(createFilmDTO.getEndDate());
        film.setActors(createFilmDTO.getActors().trim());
        film.setStudio(createFilmDTO.getStudio().trim());
        film.setDuration(createFilmDTO.getDuration());
        film.setDirectors(createFilmDTO.getDirectors().trim());
        film.setTrailer(createFilmDTO.getTrailers().trim());
        film.setCategory(createFilmDTO.getCategoryAsString().trim());
        film.setDescription(createFilmDTO.getDescription().trim());
        film.setAge(createFilmDTO.getAge());
        filmRepository.save(film);
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



