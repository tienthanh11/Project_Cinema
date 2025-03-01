package org.example.cinema_fullstack.controllers;

import org.example.cinema_fullstack.models.dto.film.FilmDTO;
import org.example.cinema_fullstack.models.dto.film.FilmTopDTO;
import org.example.cinema_fullstack.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private FilmService filmService;

    @RequestMapping("/")
    public String homepage(Model model,
                           HttpSession session,
                           @RequestParam(required = false) String nameSearch,
                           @RequestParam(required = false) Boolean isShowing) {

        List<FilmDTO> listFilmData;

        if (isShowing != null && isShowing) {
            listFilmData = filmService.listUpShowingFilmDTO();
        } else {
            listFilmData = filmService.listUpComingFilmDTO();
        }

        if (nameSearch != null && !nameSearch.isEmpty()) {
            listFilmData = listFilmData.stream()
                    .filter(listFilm -> listFilm.getName().toLowerCase().contains(nameSearch.toLowerCase()))
                    .collect(Collectors.toList());
        }

        List<FilmTopDTO> listTopFilm = filmService.getTopFilm();
        model.addAttribute("listTopFilm", listTopFilm);
        model.addAttribute("listFilmData", listFilmData);
        model.addAttribute("nameSearch", nameSearch);
        model.addAttribute("isShowing", isShowing);

        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        return "client/homepages";
    }


    @RequestMapping("/search/showing")
    public String searchUpShowingFilms(Model model, @RequestParam String nameSearch) {
        List<FilmDTO> films = filmService.searchUpShowingFilmDTO(nameSearch);
        model.addAttribute("films", films);
        return "client/homepages";
    }

    @RequestMapping("/search/upcoming")
    public String searchUpComingFilms(Model model, @RequestParam String nameSearch) {
        List<FilmDTO> films = filmService.searchUpComingFilmDTO(nameSearch);
        model.addAttribute("films", films);
        return "client/homepages";
    }

    @RequestMapping("/login")
    public String login(@RequestParam("error") Optional<String> error, Model model) {
        if (error.isPresent()) {
            model.addAttribute("error", error.get());
        }
        return "client/login";
    }

    @GetMapping("/deny")
    public String denied() {
        return "client/denied";
    }
}
