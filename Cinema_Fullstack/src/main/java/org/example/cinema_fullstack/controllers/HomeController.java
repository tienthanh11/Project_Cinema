package org.example.cinema_fullstack.controllers;

import org.example.cinema_fullstack.models.dto.film.FilmDTO;
import org.example.cinema_fullstack.models.dto.film.FilmDetailDTO;
import org.example.cinema_fullstack.models.dto.film.FilmTopDTO;
import org.example.cinema_fullstack.models.dto.film.TicketPriceDTO;
import org.example.cinema_fullstack.models.dto.ticket.BookTicketShowtimeDto;
import org.example.cinema_fullstack.services.FilmService;
import org.example.cinema_fullstack.services.AuthService;
import org.example.cinema_fullstack.services.ShowTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private FilmService filmService;

    @Autowired
    private ShowTimeService showTimeService;

    @Autowired
    private AuthService authService;

    @RequestMapping("/")
    public String homepage(Model model,
                           Authentication authentication,
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

        authService.addAuthAttributes(model, authentication);
        return "client/home/homepages";
    }

    @GetMapping("/detail/{id}")
    public String getFilmDetail(@PathVariable("id") Long id, Model model, Authentication authentication) {
        FilmDetailDTO filmDetail = filmService.getFilmById(id);
        List<BookTicketShowtimeDto> showtimes = showTimeService.getShowtimesByFilmId(id);

        model.addAttribute("filmDetail", filmDetail);
        model.addAttribute("showtimes", showtimes);

        authService.addAuthAttributes(model, authentication);
        return "client/home/film-detail";
    }

    @GetMapping("/ticket-price")
    public String getTicketPricePage(Model model, Authentication authentication) {
        List<FilmTopDTO> topFilms = filmService.getTopFilm();
        List<TicketPriceDTO> ticketPrices = filmService.listTicketPrice();

        model.addAttribute("topFilms", topFilms);
        model.addAttribute("ticketPrices", ticketPrices);

        authService.addAuthAttributes(model, authentication);
        return "client/home/ticket-price";
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
