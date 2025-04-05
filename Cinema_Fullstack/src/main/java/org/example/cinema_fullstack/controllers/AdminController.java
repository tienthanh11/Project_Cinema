package org.example.cinema_fullstack.controllers;

import org.example.cinema_fullstack.models.dto.dto_cinemaroom.ListCinemaRoomDTO;
import org.example.cinema_fullstack.models.dto.film.*;
import org.example.cinema_fullstack.services.CinemaRoomService;
import org.example.cinema_fullstack.services.FilmService;
import org.example.cinema_fullstack.validation.FilmCreateValidator;
import org.example.cinema_fullstack.validation.FilmUpdateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private FilmService filmService;

    @Autowired
    private CinemaRoomService cinemaRoomService;

    @Autowired
    private FilmCreateValidator filmCreateValidator;

    @Autowired
    private FilmUpdateValidator filmUpdateValidator;

    @InitBinder("filmDTO")
    protected void initBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(filmCreateValidator);
    }

    @InitBinder("updateFilmDTO")
    protected void initUpdateBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(filmUpdateValidator);
    }

    @GetMapping("/film")
    public String listFilms(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "8") int pageSize,
                            @RequestParam(required = false) String name,
                            Model model) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<ListFilmDTO> filmPage;

        if (name != null && !name.trim().isEmpty()) {
            filmPage = filmService.searchByName(name.trim(), pageable);
        } else {
            filmPage = filmService.getListFilm(pageable);
        }

        if (filmPage.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy dữ liệu cho '" + (name != null ? name : "") + "'");
        } else {
            model.addAttribute("listFilm", filmPage.getContent());
            model.addAttribute("page", page);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("totalPage", filmPage.getTotalPages());
            model.addAttribute("name", name);
        }
        return "admin/film/list-film";
    }

    @GetMapping("/film/detail/{id}")
    public String showFilmDetail(@PathVariable("id") long id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        FilmViewDTO film = filmService.getFilmDTOById(id);
        if (film == null) {
            redirectAttributes.addFlashAttribute("error", "Phim không tồn tại");
            return "redirect:/admin/film";
        }

        FilmDetailViewDTO filmDetail = new FilmDetailViewDTO(film);
        model.addAttribute("film", filmDetail);
        return "admin/film/detail-film";
    }


    @GetMapping("/film/create")
    public String showCreateForm(Model model) {
        model.addAttribute("filmDTO", new CreateFilmDTO());
        List<String> categories = Arrays.asList(
                "Hành động", "Hài hước", "Lãng mạn", "Tình cảm", "Viễn tưởng", "Chiến tranh", "Kiếm hiệp",
                "Âm nhạc", "Hoạt hình", "Kinh dị", "Phiêu lưu", "Võ thuật", "Kinh điển", "Tâm lý");
        model.addAttribute("categories", categories);
        return "admin/film/create-film";
    }

    @PostMapping("/film/create")
    public String createFilm(@Valid @ModelAttribute("filmDTO") CreateFilmDTO filmDTO,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<String> categories = Arrays.asList(
                    "Hành động", "Hài hước", "Lãng mạn", "Tình cảm", "Viễn tưởng", "Chiến tranh", "Kiếm hiệp",
                    "Âm nhạc", "Hoạt hình", "Kinh dị", "Phiêu lưu", "Võ thuật", "Kinh điển", "Tâm lý");
            model.addAttribute("categories", categories);
            return "admin/film/create-film";
        }

        try {
            filmService.createFilm(filmDTO);
            redirectAttributes.addFlashAttribute("success", "Thêm mới phim thành công");
            return "redirect:/admin/film";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi tạo phim: " + e.getMessage());
            List<String> categories = Arrays.asList(
                    "Hành động", "Hài hước", "Lãng mạn", "Tình cảm", "Viễn tưởng", "Chiến tranh", "Kiếm hiệp",
                    "Âm nhạc", "Hoạt hình", "Kinh dị", "Phiêu lưu", "Võ thuật", "Kinh điển", "Tâm lý");
            model.addAttribute("categories", categories);
            return "admin/film/create-film";
        }
    }

    @GetMapping("/film/update/{id}")
    public String showUpdateForm(@PathVariable("id") long id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        FilmViewDTO film = filmService.getFilmDTOById(id);
        if (film == null) {
            redirectAttributes.addFlashAttribute("error", "Phim không tồn tại");
            return "redirect:/admin/film";
        }

        UpdateFilmDTO updateFilmDTO = getUpdateFilmDTO(film);

        model.addAttribute("updateFilmDTO", updateFilmDTO);
        List<String> categories = Arrays.asList(
                "Hành động", "Hài hước", "Lãng mạn", "Tình cảm", "Viễn tưởng", "Chiến tranh", "Kiếm hiệp",
                "Âm nhạc", "Hoạt hình", "Kinh dị", "Phiêu lưu", "Võ thuật", "Kinh điển", "Tâm lý");
        model.addAttribute("categories", categories);
        return "admin/film/update-film";
    }

    private static UpdateFilmDTO getUpdateFilmDTO(FilmViewDTO film) {
        UpdateFilmDTO updateFilmDTO = new UpdateFilmDTO();
        updateFilmDTO.setId(film.getId());
        updateFilmDTO.setName(film.getName());
        updateFilmDTO.setImageURL(film.getImageURL());
        updateFilmDTO.setStartDate(film.getStartDate());
        updateFilmDTO.setEndDate(film.getEndDate());
        updateFilmDTO.setActors(film.getActors());
        updateFilmDTO.setStudio(film.getStudio());
        updateFilmDTO.setDurations(film.getDurations());
        updateFilmDTO.setDirectors(film.getDirectors());
        updateFilmDTO.setTrailers(film.getTrailers());
        updateFilmDTO.setCategory(film.getCategory() != null ? film.getCategory().split(" - ") : new String[0]);
        updateFilmDTO.setDescriptions(film.getDescriptions());
        updateFilmDTO.setAge(film.getAge());
        return updateFilmDTO;
    }

    @PostMapping("/film/update")
    public String updateFilm(@Valid @ModelAttribute("updateFilmDTO") UpdateFilmDTO updateFilmDTO,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<String> categories = Arrays.asList(
                    "Hành động", "Hài hước", "Lãng mạn", "Tình cảm", "Viễn tưởng", "Chiến tranh", "Kiếm hiệp",
                    "Âm nhạc", "Hoạt hình", "Kinh dị", "Phiêu lưu", "Võ thuật", "Kinh điển", "Tâm lý");
            model.addAttribute("categories", categories);
            return "admin/film/update-film";
        }

        try {
            filmService.updateFilm(updateFilmDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công");
            return "redirect:/admin/film";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật phim: " + e.getMessage());
            List<String> categories = Arrays.asList(
                    "Hành động", "Hài hước", "Lãng mạn", "Tình cảm", "Viễn tưởng", "Chiến tranh", "Kiếm hiệp",
                    "Âm nhạc", "Hoạt hình", "Kinh dị", "Phiêu lưu", "Võ thuật", "Kinh điển", "Tâm lý");
            model.addAttribute("categories", categories);
            return "admin/film/update-film";
        }
    }

    @PostMapping("/film/delete")
    public String deleteFilm(@RequestParam("id") long id,
                             @RequestParam(required = false) String name,
                             @RequestParam(defaultValue = "0") int page,
                             RedirectAttributes redirectAttributes) {
        try {
            filmService.deleteFilm(id);
            redirectAttributes.addFlashAttribute("success", "Xóa phim thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa phim: " + e.getMessage());
        }
        return "redirect:/admin/film?page=" + page + (name != null ? "&name=" + name : "");
    }


    @GetMapping("/cinema-room")
    public String showCinemaRoomList(Model model,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "") String name) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<ListCinemaRoomDTO> cinemaRoomPage;

        if (name.isEmpty()) {
            cinemaRoomPage = cinemaRoomService.listCinemaRoom(pageable);
        } else {
            cinemaRoomPage = cinemaRoomService.searchByName(name, pageable);
        }

        model.addAttribute("listCinemaRoom", cinemaRoomPage.getContent());
        model.addAttribute("totalPage", cinemaRoomPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("name", name);

        return "admin/cinema-room/list-cinema-room";
    }

    @GetMapping("/cinema-room/detail/{id}")
    public String showCinemaRoomDetail(@PathVariable("id") long id,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {
        ListCinemaRoomDTO cinemaRoom = cinemaRoomService.getCinemaRoomById(id);

        if (cinemaRoom == null) {
            redirectAttributes.addFlashAttribute("error", "Phòng chiếu phim không tồn tại");
            return "redirect:/admin/cinema-room";
        }

        int[] rows = IntStream.range(0, cinemaRoom.getRowSeat()).toArray();
        int[] columns = IntStream.range(0, cinemaRoom.getColumnSeat()).toArray();

        model.addAttribute("cinemaRoom", cinemaRoom);
        model.addAttribute("rows", rows);
        model.addAttribute("columns", columns);

        return "admin/cinema-room/detail-cinema-room";
    }
}
