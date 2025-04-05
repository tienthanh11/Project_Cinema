package org.example.cinema_fullstack.controllers;

import org.example.cinema_fullstack.models.dto.ticket.*;
import org.example.cinema_fullstack.models.entity.Invoice;
import org.example.cinema_fullstack.models.entity.Membership;
import org.example.cinema_fullstack.models.entity.PaymentMethod;
import org.example.cinema_fullstack.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/book")
//@SessionAttributes("selectedSeats")
@SessionAttributes({"showtime", "selectedSeats"})
public class TicketBookingController {

    @Autowired
    private ShowTimeService showTimeService;

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberShipService memberShipService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private SeatService seatService;

    @GetMapping("/film-selection")
    public String getFilmSelectionPage(@RequestParam(value = "filmId", required = false) Long filmId,
                                       @RequestParam(value = "date", required = false) String dateStr,
                                       @RequestParam(value = "showtimeId", required = false) Long showtimeId,
                                       Model model,
                                       HttpSession session,
                                       Authentication authentication) {
        List<BookTicketShowtimeDto> showtimeList = showTimeService.getAllFilmShowingThisWeek();

        model.addAttribute("showtimeList", showtimeList);
        model.addAttribute("filmList", getUniqueFilms(showtimeList));
        model.addAttribute("showDateList", getUniqueShowDates(showtimeList));
        model.addAttribute("showTimeDate", dateStr != null ? LocalDate.parse(dateStr) : null);
        model.addAttribute("showtimeId", showtimeId != null ? showtimeId : 0L);

        if (filmId != null) {
            model.addAttribute("filmId", filmId);
            List<BookTicketShowtimeDto> showtimeOfFilm = showtimeList.stream()
                    .filter(s -> s.getFilmId() != null && s.getFilmId().equals(filmId))
                    .collect(Collectors.toList());

            model.addAttribute("showDateList", getUniqueShowDates(showtimeOfFilm));

            if (dateStr != null) {
                LocalDate selectedDate = LocalDate.parse(dateStr);
                model.addAttribute("showTimeDate", selectedDate);
                List<BookTicketShowtimeDto> showtimeOnDateList = showtimeOfFilm.stream()
                        .filter(s -> s.getShowtimeDay() != null && s.getShowtimeDay().equals(selectedDate))
                        .collect(Collectors.toList());

                Map<String, List<BookTicketShowtimeDto>> showtimesByTechAndSubtitle = showtimeOnDateList.stream()
                        .filter(s -> s.getFilmTechnology() != null && !s.getFilmTechnology().trim().isEmpty() &&
                                s.getSubtitle() != null && !s.getSubtitle().trim().isEmpty())
                        .collect(Collectors.groupingBy(s -> s.getFilmTechnology().trim() + "-" + s.getSubtitle().trim()));

                model.addAttribute("showtimesByTechAndSubtitle", showtimesByTechAndSubtitle);
            }
        } else if (dateStr != null && filmId == null) {
            return "redirect:/book/film-selection";
        }

        authService.addAuthAttributes(model, authentication);
        return "client/ticket-booking/film-selection";
    }

    private List<BookTicketFilmDto> getUniqueFilms(List<BookTicketShowtimeDto> showtimeList) {
        return showtimeList.stream()
                .filter(s -> s.getFilmId() != null)
                .collect(Collectors.groupingBy(BookTicketShowtimeDto::getFilmId))
                .values().stream()
                .map(list -> list.get(0))
                .map(s -> new BookTicketFilmDto(
                        s.getFilmId(),
                        s.getFilmName(),
                        s.getFilmCategory(),
                        s.getFilmDuration(),
                        s.getFilmImageUrl()))
                .collect(Collectors.toList());
    }

    private List<LocalDate> getUniqueShowDates(List<BookTicketShowtimeDto> showtimeList) {
        return showtimeList.stream()
                .map(BookTicketShowtimeDto::getShowtimeDay)
                .filter(day -> day != null)
                .distinct()
                .collect(Collectors.toList());
    }


    @ModelAttribute("selectedSeats")
    public List<SeatDto> selectedSeats() {
        return new ArrayList<>();
    }

    @GetMapping("/seat-selection")
    public String getSeatSelectionPage(@RequestParam(value = "showtimeId", required = true) Long showtimeId,
                                       @RequestParam(value = "error", required = false) String error,
                                       Model model,
                                       HttpSession session,
                                       Authentication authentication) {
        ShowtimeDto showtime = showTimeService.getShowtimeById(showtimeId);
        if (showtime == null) {
            return "redirect:/book/film-selection";
        }

        List<SeatDto> seatList = showTimeService.getAllSeatByShowtimeId(showtimeId);
        CinemaRoomLayoutDto layout = showTimeService.getCinemaRoomLayoutByShowtimeId(showtimeId);

        // Prepare rows and columns for the seat layout
        List<Integer> rows = IntStream.range(0, layout.getRowSeat()).boxed().collect(Collectors.toList());
        List<Integer> columns = IntStream.range(0, layout.getColumnSeat()).boxed().collect(Collectors.toList());

        // Get selected seats from session
        List<SeatDto> selectedSeats = (List<SeatDto>) session.getAttribute("selectedSeats");
        if (selectedSeats == null) {
            selectedSeats = new ArrayList<>();
            session.setAttribute("selectedSeats", selectedSeats);
        }

        // Compute classes for each seat
        List<String> seatClasses = new ArrayList<>();
        for (int i = 0; i < rows.size() * columns.size(); i++) {
            StringBuilder classes = getStringBuilder(i, seatList, selectedSeats);
            seatClasses.add(classes.toString().trim());
        }

        // Compute total price of selected seats
        int totalPrice = 0;
        if (selectedSeats != null) {
            for (SeatDto seat : selectedSeats) {
                if (seat.getPrice() != null) {
                    totalPrice += seat.getPrice();
                }
            }
        }

        model.addAttribute("showtime", showtime);
        model.addAttribute("seatList", seatList);
        model.addAttribute("layout", layout);
        model.addAttribute("rows", rows);
        model.addAttribute("columns", columns);
        model.addAttribute("selectedSeats", selectedSeats);
        model.addAttribute("seatClasses", seatClasses);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("error", error);

        authService.addAuthAttributes(model, authentication);
        return "client/ticket-booking/seat-selection";
    }

    private static StringBuilder getStringBuilder(int i, List<SeatDto> seatList, List<SeatDto> selectedSeats) {
        SeatDto seat = (i < seatList.size()) ? seatList.get(i) : null;
        StringBuilder classes = new StringBuilder();
        if (seat != null) {
            if ("n".equals(seat.getSeatCode())) classes.append("seat-none ");
            if ("v".equals(seat.getSeatCode())) classes.append("vip-seat ");
            if ("s".equals(seat.getSeatCode())) classes.append("standard-seat ");
            if (seat.getTicketId() != null) classes.append("booked-seat ");
            if (selectedSeats != null && seat != null && selectedSeats.contains(seat)) classes.append("keeping-seat ");
        } else {
            classes.append("seat-none ");
        }
        return classes;
    }

    @PostMapping("/select-seat")
    public String selectSeat(@RequestParam Long seatId,
                             @SessionAttribute("selectedSeats") List<SeatDto> selectedSeats,
                             @RequestParam Long showtimeId,
                             Model model,
                             Authentication authentication) {
        List<SeatDto> seatList = showTimeService.getAllSeatByShowtimeId(showtimeId);
        SeatDto seat = seatList.stream().filter(s -> s.getId().equals(seatId)).findFirst().orElse(null);

        if (seat != null && !"n".equals(seat.getSeatCode()) && !"d".equals(seat.getSeatCode()) && seat.getTicketId() == null) {
            if (selectedSeats.contains(seat)) {
                selectedSeats.remove(seat);
            } else if (selectedSeats.size() < 8) {
                selectedSeats.add(seat);
            }
        }

        authService.addAuthAttributes(model, authentication);
        return "redirect:/book/seat-selection?showtimeId=" + showtimeId;
    }

    @GetMapping("/back")
    public String back(@SessionAttribute("selectedSeats") List<SeatDto> selectedSeats,
                       Model model,
                       Authentication authentication) {
        selectedSeats.clear();
        authService.addAuthAttributes(model, authentication);
        return "redirect:/book/film-selection";
    }

    @GetMapping("/next")
    public String next(@SessionAttribute("selectedSeats") List<SeatDto> selectedSeats,
                       @RequestParam Long showtimeId,
                       Model model,
                       Authentication authentication) {
        if (selectedSeats.isEmpty()) {
            return "redirect:/book/seat-selection?showtimeId=" + showtimeId + "&error=selectSeat";
        }
        authService.addAuthAttributes(model, authentication);
        return "redirect:/book/booking-confirmation";
    }

    @ModelAttribute("showtime")
    public ShowtimeDto showtime() {
        return null;
    }

    @GetMapping("/booking-confirmation")
    public String showBookingConfirmation(Model model,
                                          @ModelAttribute("showtime") ShowtimeDto showtime,
                                          @ModelAttribute("selectedSeats") List<SeatDto> selectedSeats,
                                          Authentication authentication,
                                          HttpSession session) {
        if (showtime == null || selectedSeats == null || selectedSeats.isEmpty()) {
            return "redirect:/book/film-selection";
        }

        Membership membership = null;
        if (authentication != null) {
            membership = memberShipService.findByUsername(authentication.getName());
        }

        List<PaymentMethod> paymentMethods = paymentMethodService.getAllPaymentMethod();
        double totalAmount = selectedSeats.stream()
                .mapToDouble(seat -> seat.getPrice() != null ? seat.getPrice() : 0)
                .sum();

        model.addAttribute("showtime", showtime);
        model.addAttribute("selectedSeats", selectedSeats);
        model.addAttribute("paymentMethods", paymentMethods);
        model.addAttribute("membership", membership);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("bookingInformation", new BookingInformation());

        authService.addAuthAttributes(model, authentication);
        return "client/ticket-booking/booking-confirmation";
    }

    @PostMapping("/confirm-booking")
    public String processBookingConfirmation(@ModelAttribute BookingInformation bookingInformation,
                                             @ModelAttribute("showtime") ShowtimeDto showtime,
                                             @ModelAttribute("selectedSeats") List<SeatDto> selectedSeats,
                                             Model model,
                                             Authentication authentication,
                                             RedirectAttributes redirectAttributes,
                                             HttpSession session) {
        if (bookingInformation.getPaymentMethodId() == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn hình thức thanh toán");
            return "redirect:/book/booking-confirmation";
        }

        Membership membership = memberShipService.findByUsername(authentication.getName());
        if (membership == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để tiếp tục đặt vé");
            return "redirect:/login?redirect=/book/booking-confirmation";
        }

        bookingInformation.setShowtimeId(showtime.getShowtimeId());
        bookingInformation.setMemberId(membership.getId());
        bookingInformation.setSeatIdList(selectedSeats.stream().map(SeatDto::getId).collect(Collectors.toList()));

        try {
            invoiceService.checkSeatAvailable(bookingInformation);
            switch (bookingInformation.getPaymentMethodId().intValue()) {
                case 1: // PayPal
                    PaymentDTO payment = new PaymentDTO();
                    payment.setAmount(selectedSeats.stream()
                            .mapToDouble(seat -> seat.getPrice() != null ? seat.getPrice() : 0)
                            .sum());
                    session.setAttribute("paymentDTO", payment);
                    session.setAttribute("bookingInformation", bookingInformation);

                    authService.addAuthAttributes(model, authentication);
                    return "redirect:/paypal/pay";
                case 2: // Direct payment
                    Invoice invoice = invoiceService.createInvoice(
                            bookingInformation.getMemberId(),
                            bookingInformation.getPaymentMethodId(),
                            invoiceService.generateCode());
                    for (Long seatId : bookingInformation.getSeatIdList()) {
                        long ticketId = ticketService.createTicket(invoice.getId());
                        seatService.updateTicketIdBySeatId(ticketId, seatId);
                    }
                    redirectAttributes.addAttribute("invoiceId", invoice.getId());

                    authService.addAuthAttributes(model, authentication);
                    return "redirect:/book/member/booking-information";
                default:
                    redirectAttributes.addFlashAttribute("error", "Phương thức thanh toán không hợp lệ");

                    authService.addAuthAttributes(model, authentication);
                    return "redirect:/book/booking-confirmation";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            return "redirect:/book/seat-selection?showtimeId=" + showtime.getShowtimeId();
        }
    }


    @Transactional
    @GetMapping("/create-booking")
    public String createInvoice(HttpSession session,
                                Model model,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        BookingInformation bookingInformation = (BookingInformation) session.getAttribute("bookingInformation");

        if (bookingInformation == null || bookingInformation.getSeatIdList() == null || bookingInformation.getSeatIdList().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Thông tin đặt vé không hợp lệ");
            return "redirect:/book/booking-confirmation";
        }

        try {
            Invoice invoice = invoiceService.createInvoice(
                    bookingInformation.getMemberId(),
                    bookingInformation.getPaymentMethodId(),
                    invoiceService.generateCode()
            );
            for (Long seatId : bookingInformation.getSeatIdList()) {
                long ticketId = ticketService.createTicket(invoice.getId());
                seatService.updateTicketIdBySeatId(ticketId, seatId);
            }
            redirectAttributes.addAttribute("invoiceId", invoice.getId());
            redirectAttributes.addFlashAttribute("message", "Đặt vé thành công qua PayPal");

            session.removeAttribute("bookingInformation");
            session.removeAttribute("paymentDTO");

            authService.addAuthAttributes(model, authentication);
            return "redirect:/book/member/booking-information";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/book/booking-confirmation";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi tạo đặt vé: " + e.getMessage());
            return "redirect:/book/booking-confirmation";
        }
    }

    @GetMapping("/back-to-seat-selection")
    public String backToSeatSelection(@ModelAttribute("showtime") ShowtimeDto showtime) {
        return "redirect:/book/seat-selection?showtimeId=" + showtime.getShowtimeId();
    }

    @GetMapping("/member/booking-information")
    public String showBookingInformation(@RequestParam("invoiceId") Long invoiceId,
                                         Authentication authentication,
                                         Model model,
                                         RedirectAttributes redirectAttributes) {
        BookingInvoiceDTO invoice = invoiceService.getInvoiceByInvoiceId(invoiceId);
        if (invoice == null) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
            return "redirect:/book/film-selection";
        }

        if (authentication == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để xem thông tin đặt vé");
            return "redirect:/login?redirect=/book/booking-information?invoiceId=" + invoiceId;
        }

        Membership membership = memberShipService.findByUsername(authentication.getName());
        if (membership == null || !invoice.getMemberId().equals(membership.getId())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền truy cập trang này");
            return "redirect:/book/film-selection";
        }

        BookTicketShowtimeDto showtime = showTimeService.getShowtimeByInvoiceId(invoiceId);
        if (showtime == null) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
            return "redirect:/book/film-selection";
        }

        List<BookingTicketDTO> ticketList = ticketService.getAllTicketByInvoiceId(invoiceId);
        if (ticketList == null) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
            return "redirect:/book/film-selection";
        }

        double totalAmount = ticketList.stream()
                .mapToDouble(BookingTicketDTO::getPrice)
                .sum();

        model.addAttribute("invoice", invoice);
        model.addAttribute("showtime", showtime);
        model.addAttribute("ticketList", ticketList);
        model.addAttribute("totalAmount", totalAmount);

        authService.addAuthAttributes(model, authentication);
        return "client/ticket-booking/booking-information";
    }
}
