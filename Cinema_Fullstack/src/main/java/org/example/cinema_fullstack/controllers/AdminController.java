package org.example.cinema_fullstack.controllers;

import com.itextpdf.html2pdf.HtmlConverter;
import org.example.cinema_fullstack.models.dto.dto_cinemaroom.ListCinemaRoomDTO;
import org.example.cinema_fullstack.models.dto.film.*;
import org.example.cinema_fullstack.models.dto.membership.MembershipUpdateDTO;
import org.example.cinema_fullstack.models.dto.showtime.CreateShowtimeDTO;
import org.example.cinema_fullstack.models.dto.showtime.CreateShowtimeSeatDTO;
import org.example.cinema_fullstack.models.dto.showtime.ShowTimeDataDTO;
import org.example.cinema_fullstack.models.dto.ticket.TicketMemberDTO;
import org.example.cinema_fullstack.models.entity.*;
import org.example.cinema_fullstack.repositories.DistrictRepository;
import org.example.cinema_fullstack.repositories.ProvinceRepository;
import org.example.cinema_fullstack.repositories.WardRepository;
import org.example.cinema_fullstack.services.*;
import org.example.cinema_fullstack.validation.FilmCreateValidator;
import org.example.cinema_fullstack.validation.FilmUpdateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private FilmService filmService;

    @Autowired
    private CinemaRoomService cinemaRoomService;

    @Autowired
    private ShowTimeService showtimeService;

    @Autowired
    private TicketPriceService ticketPriceService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private MemberShipService memberShipService;

    @Autowired
    private WardRepository wardRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

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
    public String showFilmCreateForm(Model model) {
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

        int rowSeat = cinemaRoom.getRowSeat();
        int columnSeat = cinemaRoom.getColumnSeat();
        int[] rows = IntStream.range(0, rowSeat).toArray();
        int[] columns = IntStream.range(0, columnSeat).toArray();

        // Compute tooltips and seat classes
        String[][] tooltips = new String[rowSeat][columnSeat];
        String[][] seatClasses = new String[rowSeat][columnSeat];
        String seatLayout = cinemaRoom.getSeatLayout();

        // Validate seatLayout length
        int expectedLength = rowSeat * columnSeat;
        if (seatLayout == null || seatLayout.length() < expectedLength) {
            StringBuilder paddedSeatLayout = new StringBuilder(seatLayout != null ? seatLayout : "");
            while (paddedSeatLayout.length() < expectedLength) {
                paddedSeatLayout.append('n');
            }
            seatLayout = paddedSeatLayout.toString();
        }

        for (int row = 0; row < rowSeat; row++) {
            for (int column = 0; column < columnSeat; column++) {
                int index = row * columnSeat + column;
                char seatTypeChar = seatLayout.charAt(index);

                String seatType;
                if (seatTypeChar == 's') {
                    seatType = "Thường";
                } else if (seatTypeChar == 'v') {
                    seatType = "VIP";
                } else if (seatTypeChar == 'd') {
                    seatType = "Hỏng";
                } else {
                    seatType = "Không có";
                }
                tooltips[row][column] = "Ghế " + (row + 1) + "-" + (column + 1) + " (" + seatType + ")";

                String seatClass;
                if (seatTypeChar == 'n') {
                    seatClass = "seat-none";
                } else if (seatTypeChar == 's') {
                    seatClass = "standard-seat";
                } else if (seatTypeChar == 'v') {
                    seatClass = "vip-seat";
                } else if (seatTypeChar == 'd') {
                    seatClass = "damaged-seat";
                } else {
                    seatClass = "";
                }
                seatClasses[row][column] = seatClass;
            }
        }

        model.addAttribute("cinemaRoom", cinemaRoom);
        model.addAttribute("rows", rows);
        model.addAttribute("columns", columns);
        model.addAttribute("tooltips", tooltips);
        model.addAttribute("seatClasses", seatClasses);

        return "admin/cinema-room/detail-cinema-room";
    }

    private static final List<String> TIME_SLOTS = Arrays.asList(
            "11:00:00", "13:30:00", "16:00:00", "18:30:00", "21:00:00", "23:30:00"
    );

    @GetMapping("/showtime/create")
    public String showShowTimeCreateForm(Model model) {
        ShowTimeDataDTO showtimeData = new ShowTimeDataDTO();
        List<CreateShowtimeDTO> showtimeList = new ArrayList<>();
        showtimeList.add(new CreateShowtimeDTO());
        showtimeData.setShowtimeList(showtimeList);
        model.addAttribute("showtimeData", showtimeData);
        model.addAttribute("films", showtimeService.getAllFilmAvailable());
        model.addAttribute("cinemaRooms", showtimeService.findAllCinemaRoom());
        model.addAttribute("timeSlots", TIME_SLOTS);
        model.addAttribute("technologies", Arrays.asList("2D", "3D"));
        model.addAttribute("subTitles", Arrays.asList("Phụ đề", "Lồng tiếng"));
        return "admin/showtime/create-showtime";
    }


    @PostMapping("/showtime/create")
    @Transactional
    public String createShowtime(@ModelAttribute("showtimeData") ShowTimeDataDTO showtimeData,
                                 Model model) {
        model.addAttribute("films", showtimeService.getAllFilmAvailable());
        model.addAttribute("cinemaRooms", showtimeService.findAllCinemaRoom());
        model.addAttribute("timeSlots", TIME_SLOTS);
        model.addAttribute("technologies", Arrays.asList("2D", "3D"));
        model.addAttribute("subTitles", Arrays.asList("Phụ đề", "Lồng tiếng"));

        CreateShowtimeDTO firstShowtime = showtimeData.getShowtimeList().get(0);
        if (firstShowtime.getFilmId() == null || firstShowtime.getFilmId() == 0) {
            model.addAttribute("errorMessage", "Phim không được để trống");
            return "admin/showtime/create-showtime";
        }
        if (firstShowtime.getDay() == null) {
            model.addAttribute("errorMessage", "Ngày chiếu không được để trống");
            return "admin/showtime/create-showtime";
        }
        if (firstShowtime.getCinemaRoomId() == null || firstShowtime.getCinemaRoomId() == 0) {
            model.addAttribute("errorMessage", "Phòng chiếu không được để trống");
            return "admin/showtime/create-showtime";
        }
        if (firstShowtime.getFilmTechnology() == null || firstShowtime.getFilmTechnology().isEmpty()) {
            model.addAttribute("errorMessage", "Công nghệ không được để trống");
            return "admin/showtime/create-showtime";
        }
        if (firstShowtime.getSubTitle() == null || firstShowtime.getSubTitle().isEmpty()) {
            model.addAttribute("errorMessage", "Ngôn ngữ không được để trống");
            return "admin/showtime/create-showtime";
        }

        List<String> selectedTimes = showtimeData.getSelectedTimes();
        if (selectedTimes == null || selectedTimes.isEmpty()) {
            model.addAttribute("errorMessage", "Vui lòng chọn ít nhất một giờ chiếu");
            return "admin/showtime/create-showtime";
        }

        LocalDate today = LocalDate.now();
        if (firstShowtime.getDay().isBefore(today)) {
            model.addAttribute("errorMessage", "Ngày chiếu phải là ngày trong tương lai");
            return "admin/showtime/create-showtime";
        }

        try {
            CinemaRoom room = showtimeService.findById(firstShowtime.getCinemaRoomId());
            List<CreateShowtimeSeatDTO> seatList = generateSeatList(room);
            showtimeData.setSeatList(seatList);

            List<CreateShowtimeDTO> showtimeList = new ArrayList<>();
            for (String time : selectedTimes) {
                CreateShowtimeDTO showtime = new CreateShowtimeDTO();
                showtime.setFilmId(firstShowtime.getFilmId());
                showtime.setCinemaRoomId(firstShowtime.getCinemaRoomId());
                showtime.setDay(firstShowtime.getDay());
                showtime.setTime(LocalTime.parse(time));
                showtime.setFilmTechnology(firstShowtime.getFilmTechnology());
                showtime.setSubTitle(firstShowtime.getSubTitle());
                showtimeList.add(showtime);
            }
            showtimeData.setShowtimeList(showtimeList);

            for (CreateShowtimeDTO showtime : showtimeData.getShowtimeList()) {
                if (!showtimeService.checkShowtimeAvailable(
                        showtime.getCinemaRoomId(), showtime.getDay(), showtime.getTime()).isEmpty()) {
                    model.addAttribute("errorMessage",
                            "Đã có suất chiếu vào lúc " + showtime.getTime() +
                                    ", ngày " + showtime.getDay().getDayOfMonth() + "-" +
                                    showtime.getDay().getMonthValue() + "-" + showtime.getDay().getYear());
                    return "admin/showtime/create-showtime";
                }

                showtimeService.createShowtimeDTO(showtime);
                Showtime createdShowtime = showtimeService.findShowtimeById(showtimeService.getMaxByIdShowtime());

                for (CreateShowtimeSeatDTO seat : showtimeData.getSeatList()) {
                    if (!seat.getName().isEmpty()) {
                        long ticketPriceId = ticketPriceService.getTicketPriceBySeatCode(seat.getCode()).getId();
                        showtimeService.createSeat(seat.getName(), createdShowtime.getId(), ticketPriceId);
                    }
                }
            }

            model.addAttribute("successMessage", "Tạo mới thành công suất chiếu");
            // Reset form
            showtimeData.setShowtimeList(new ArrayList<>(List.of(new CreateShowtimeDTO())));
            showtimeData.setSelectedTimes(new ArrayList<>());
            return "admin/showtime/create-showtime";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Không thể tạo suất chiếu: " + e.getMessage());
            return "admin/showtime/create-showtime";
        }
    }

    private List<CreateShowtimeSeatDTO> generateSeatList(CinemaRoom room) {
        List<CreateShowtimeSeatDTO> seatList = new ArrayList<>();
        for (int row = 0; row < room.getRowSeat(); row++) {
            for (int column = 0; column < room.getColumnSeat(); column++) {
                CreateShowtimeSeatDTO seat = new CreateShowtimeSeatDTO();
                String seatCode = String.valueOf(room.getSeatLayout().charAt(row * room.getColumnSeat() + column));
                if (!seatCode.equals("n")) {
                    seat.setName(getSeatName(row, column, room));
                    seat.setCode(seatCode);
                } else {
                    seat.setName("");
                    seat.setCode(seatCode);
                }
                seatList.add(seat);
            }
        }
        return seatList;
    }

    private String getSeatName(int row, int column, CinemaRoom room) {
        int r = 0;
        int spaceRow = 0;
        String seatLayout = room.getSeatLayout();
        int columnSeat = room.getColumnSeat();

        for (int i = 0; i <= column; i++) {
            if (seatLayout.charAt(row * columnSeat + i) != 'n') {
                r++;
            }
        }

        for (int j = 0; j <= row; j++) {
            int countSpace = 0;
            for (int k = 0; k < columnSeat; k++) {
                if (seatLayout.charAt(j * columnSeat + k) == 'n') {
                    countSpace++;
                }
            }
            if (countSpace == columnSeat) {
                spaceRow++;
            }
        }

        if (seatLayout.charAt(row * columnSeat + column) != 'n') {
            return String.valueOf((char) (65 + row - spaceRow)) + r;
        }
        return "";
    }


    @GetMapping("/member-ticket")
    public String showTicketList(@RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "phone", defaultValue = "") String phone,
                                 Model model) {
        page = Math.max(0, page);
        Pageable pageable = PageRequest.of(page, 10);
        Page<TicketMemberDTO> ticketPage;

        try {
            if (phone.isEmpty()) {
                ticketPage = ticketService.findAllTicket(pageable);
            } else {
                ticketPage = ticketService.findAllTicketBySearch(phone, pageable);
            }
        } catch (Exception e) {
            // Handle service errors by returning an empty page
            ticketPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        model.addAttribute("ticketPage", ticketPage);
        model.addAttribute("phone", phone);
        model.addAttribute("currentPage", page);
        return "admin/ticket/member-ticket";
    }

    @PostMapping("/member-ticket/print")
    @Transactional
    public String printTicket(@RequestParam("invoiceId") Long invoiceId,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "phone", defaultValue = "") String phone,
                              Model model) {
        ticketService.updatePrintedTicket(invoiceId);
        return "redirect:/admin/member-ticket?page=" + page + "&phone=" + phone;
    }

    @GetMapping("/member-ticket/download")
    public ResponseEntity<Resource> downloadTicket(@RequestParam("invoiceId") Long invoiceId) throws Exception {
        TicketMemberDTO invoice = ticketService.getInvoiceMember(invoiceId);

        String htmlContent = generateInvoiceHtml(invoice);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, outputStream);

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=ticket_" + invoice.getCode() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(outputStream.size())
                .body(resource);
    }

    private String generateInvoiceHtml(TicketMemberDTO invoice) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = invoice.getDay().format(dateFormatter);

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; }" +
                "h4 { text-align: center; }" +
                "table { width: 100%; border-collapse: collapse; }" +
                "td { padding: 8px; border: 1px solid #ddd; }" +
                ".bg-light { background-color: #f8f9fa; }" +
                ".text-danger { color: red; }" +
                ".text-uppercase { text-transform: uppercase; }" +
                ".font-weight-bold { font-weight: bold; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h4>THÔNG TIN ĐẶT VÉ</h4>" +
                "<table>" +
                "<tr class='bg-light'>" +
                "<td>MÃ ĐẶT VÉ:</td>" +
                "<td class='font-weight-bold text-danger'>" + invoice.getCode() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td colspan='2' class='bg-light'>THÔNG TIN PHIM:</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Phim:</td>" +
                "<td class='text-uppercase text-danger font-weight-bold'>" + invoice.getFilmName() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Ngày chiếu:</td>" +
                "<td class='font-weight-bold'>" + formattedDate + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Giờ chiếu:</td>" +
                "<td class='font-weight-bold'>" + invoice.getTime() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Rạp:</td>" +
                "<td class='font-weight-bold'>" + invoice.getCinemaRoom() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Ghế:</td>" +
                "<td class='font-weight-bold'>" + invoice.getSeatName() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Tổng cộng</td>" +
                "<td class='font-weight-bold'>" + invoice.getPrice() + " VND</td>" +
                "</tr>" +
                "<tr>" +
                "<td colspan='2' class='bg-light'>THÔNG TIN KHÁCH HÀNG:</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Mã thành viên:</td>" +
                "<td>" + invoice.getMemberCode() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Họ và tên:</td>" +
                "<td>" + invoice.getMemberName() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>SĐT:</td>" +
                "<td>" + invoice.getMemberPhone() + "</td>" +
                "</tr>" +
                "</table>" +
                "</body>" +
                "</html>";
    }

    @GetMapping("/memberships")
    public String getAllMemberships(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "") String search,
                                    Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Membership> membershipPage;

        if (search.isEmpty()) {
            membershipPage = memberShipService.findAllWithPagination(pageable);
        } else {
            membershipPage = memberShipService.searchByName(search, pageable);
        }

        model.addAttribute("memberships", membershipPage.getContent());
        model.addAttribute("currentPage", membershipPage.getNumber());
        model.addAttribute("totalPages", membershipPage.getTotalPages());
        model.addAttribute("totalItems", membershipPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("search", search);
        return "admin/membership/membership-list";
    }


    @GetMapping("/memberships/detail/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        Membership membership = memberShipService.getMembershipDetail(id);
        if (membership == null) {
            model.addAttribute("error", "Thành viên không tồn tại!");
            return "redirect:/admin/memberships";
        }
        model.addAttribute("membership", membership);
        return "admin/membership/membership-detail";
    }

    @GetMapping("/memberships/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Membership membership = memberShipService.findById(id);
        if (membership == null) {
            model.addAttribute("error", "Thành viên không tồn tại!");
            return "redirect:/admin/memberships";
        }

        MembershipUpdateDTO membershipUpdateDTO = new MembershipUpdateDTO();
        membershipUpdateDTO.setId(membership.getId());
        membershipUpdateDTO.setMemberCode(membership.getMemberCode());
        membershipUpdateDTO.setName(membership.getName());
        membershipUpdateDTO.setCard(membership.getCard());
        membershipUpdateDTO.setPhone(membership.getPhone());
        membershipUpdateDTO.setEmail(membership.getEmail());
        membershipUpdateDTO.setBirthday(membership.getBirthday());
        membershipUpdateDTO.setGender(membership.getGender());
        membershipUpdateDTO.setImageURL(membership.getImageURL());
        membershipUpdateDTO.setWardId(membership.getWard() != null ? membership.getWard().getId() : null);
        membershipUpdateDTO.setDistrictId(membership.getWard() != null && membership.getWard().getDistrict() != null ? membership.getWard().getDistrict().getId() : null);
        membershipUpdateDTO.setProvinceId(membership.getWard() != null && membership.getWard().getDistrict() != null && membership.getWard().getDistrict().getProvince() != null ? membership.getWard().getDistrict().getProvince().getId() : null);

        // Fetch all provinces, districts, and wards
        List<Province> provinces = provinceRepository.findAll();
        List<District> districts = districtRepository.findAll();
        List<Ward> wards = wardRepository.findAll();

        // Log the data to verify
        logger.info("Provinces loaded: {}", provinces.size());
        logger.info("Districts loaded: {}", districts.size());
        logger.info("Wards loaded: {}", wards.size());
        logger.info("MembershipUpdateDTO: provinceId={}, districtId={}, wardId={}",
                membershipUpdateDTO.getProvinceId(),
                membershipUpdateDTO.getDistrictId(),
                membershipUpdateDTO.getWardId());

        model.addAttribute("membership", membership);
        model.addAttribute("membershipUpdateDTO", membershipUpdateDTO);
        model.addAttribute("provinces", provinces);
        model.addAttribute("districts", districts);
        model.addAttribute("wards", wards);
        return "admin/membership/membership-update";
    }


    @PostMapping("/memberships/update/{id}")
    public String updateMembership(@PathVariable Long id,
                                   @Valid @ModelAttribute("membershipUpdateDTO") MembershipUpdateDTO membershipUpdateDTO,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            Membership membership = memberShipService.findById(id);
            List<Province> provinces = provinceRepository.findAll();
            List<District> districts = districtRepository.findAll();
            List<Ward> wards = wardRepository.findAll();
            model.addAttribute("membership", membership);
            model.addAttribute("provinces", provinces);
            model.addAttribute("districts", districts);
            model.addAttribute("wards", wards);
            return "admin/membership/membership-update";
        }

        Membership updatedMembership = memberShipService.updateMembership(id, membershipUpdateDTO);
        if (updatedMembership == null) {
            model.addAttribute("error", "Cập nhật thành viên thất bại!");
            return "redirect:/admin/memberships/update/" + id;
        }
        model.addAttribute("success", "Cập nhật thành viên thành công!");
        return "redirect:/admin/memberships";
    }

    @PostMapping("/memberships/delete")
    public String deleteMembership(@RequestParam Long id, Model model) {
        boolean deleted = memberShipService.deleteMembership(id);
        if (!deleted) {
            model.addAttribute("error", "Xóa thành viên thất bại!");
        } else {
            model.addAttribute("success", "Xóa thành viên thành công!");
        }
        return "redirect:/admin/memberships";
    }
}
