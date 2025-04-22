package org.example.cinema_fullstack.controllers;

import org.example.cinema_fullstack.models.dto.account.AccountDTO;
import org.example.cinema_fullstack.models.dto.account.AccountMemberDTO;
import org.example.cinema_fullstack.models.dto.account.ResetPasswordDTO;
import org.example.cinema_fullstack.models.dto.film.FilmDTO;
import org.example.cinema_fullstack.models.dto.film.FilmDetailDTO;
import org.example.cinema_fullstack.models.dto.film.FilmTopDTO;
import org.example.cinema_fullstack.models.dto.film.TicketPriceDTO;
import org.example.cinema_fullstack.models.dto.ticket.BookTicketShowtimeDto;
import org.example.cinema_fullstack.models.entity.Account;
import org.example.cinema_fullstack.models.entity.Membership;
import org.example.cinema_fullstack.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRoleService accountRoleService;

    @Autowired
    private MemberShipService memberShipService;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private WardService wardService;

    private static String UPLOADED_FOLDER = "src/main/resources/static/uploads/";

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

    @RequestMapping("/film-list")
    public String getFilmList(Model model,
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

        model.addAttribute("listFilmData", listFilmData);
        model.addAttribute("nameSearch", nameSearch);
        model.addAttribute("isShowing", isShowing);

        authService.addAuthAttributes(model, authentication);
        return "client/home/film-list";
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

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("accountMemberDTO", new AccountMemberDTO());
        model.addAttribute("provinces", provinceService.findAll());
        model.addAttribute("districts", districtService.findAllByProvinceId(1));
        model.addAttribute("wards", wardService.findAllByDistrictId(1));
        model.addAttribute("genders", List.of("Nam", "Nữ", "Khác"));
        model.addAttribute("listError", new HashMap<String, String>());
        return "client/register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("accountMemberDTO") AccountMemberDTO accountMemberDTO,
                                  BindingResult bindingResult,
                                  @RequestParam("imageFile") MultipartFile imageFile,
                                  Model model) {
        Map<String, String> listError = new HashMap<>();
        model.addAttribute("listError", listError);

        if (bindingResult.hasErrors()) {
            model.addAttribute("provinces", provinceService.findAll());
            model.addAttribute("districts", districtService.findAllByProvinceId(accountMemberDTO.getProvinceId() != null ? accountMemberDTO.getProvinceId() : 1));
            model.addAttribute("wards", wardService.findAllByDistrictId(accountMemberDTO.getDistrictId() != null ? accountMemberDTO.getDistrictId() : 1));
            model.addAttribute("genders", List.of("Nam", "Nữ", "Khác"));
            return "client/register";
        }

        if (memberShipService.findByEmail(accountMemberDTO.getEmail()) != null) {
            listError.put("existEmail", "Email đã tồn tại, vui lòng nhập email khác!");
        }
        if (accountService.findByUsername(accountMemberDTO.getUsername()) != null) {
            listError.put("existUsername", "Tên đăng nhập đã tồn tại, vui lòng nhập tên đăng nhập khác!");
        }
        if (memberShipService.findByCard(accountMemberDTO.getCard()) != null) {
            listError.put("existCard", "CMND đã tồn tại, vui lòng nhập CMND khác!");
        }
        if (memberShipService.findByPhone(accountMemberDTO.getPhone()) != null) {
            listError.put("existPhone", "Số điện thoại đã tồn tại, vui lòng nhập số điện thoại khác!");
        }
        if (!accountMemberDTO.getPassword().equals(accountMemberDTO.getConfirmPassword())) {
            listError.put("confirmPassword", "Mật khẩu xác nhận không khớp!");
        }

        if (!listError.isEmpty()) {
            return getString(accountMemberDTO, model, listError);
        }

        String imageURL = null;
        if (!imageFile.isEmpty()) {
            try {
                byte[] bytes = imageFile.getBytes();
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path path = Paths.get(UPLOADED_FOLDER + fileName);
                Files.write(path, bytes);
                imageURL = "/uploads/" + fileName;
            } catch (IOException e) {
                listError.put("imageError", "Lỗi khi upload hình ảnh!");
                return getString(accountMemberDTO, model, listError);
            }
        }

        accountMemberDTO.setImageURL(imageURL);
        accountMemberDTO.setMemberCode(generateMemberCode());

        try {
            accountService.createAccount(accountMemberDTO.getUsername(), accountMemberDTO.getPassword());
            memberShipService.createMembership(accountMemberDTO);
            Account account = accountService.findByUsername(accountMemberDTO.getUsername());
            accountRoleService.createAccountRole(account.getId(), 2);
            return "redirect:/login?registerSuccess=true";
        } catch (Exception e) {
            listError.put("generalError", "Đăng ký thất bại, vui lòng thử lại!");
            return getString(accountMemberDTO, model, listError);
        }
    }

    private String getString(@ModelAttribute("accountMemberDTO") @Valid AccountMemberDTO accountMemberDTO, Model model, Map<String, String> listError) {
        model.addAttribute("listError", listError);
        model.addAttribute("provinces", provinceService.findAll());
        model.addAttribute("districts", districtService.findAllByProvinceId(accountMemberDTO.getProvinceId() != null ? accountMemberDTO.getProvinceId() : 1));
        model.addAttribute("wards", wardService.findAllByDistrictId(accountMemberDTO.getDistrictId() != null ? accountMemberDTO.getDistrictId() : 1));
        model.addAttribute("genders", List.of("Nam", "Nữ", "Khác"));
        return "client/register";
    }

    private String generateMemberCode() {
        int number1 = (int) (1000 + Math.random() * 9000);
        int number2 = (int) (1000 + Math.random() * 9000);
        int number3 = (int) (1000 + Math.random() * 9000);
        int number4 = (int) (1000 + Math.random() * 9000);
        return number1 + "-" + number2 + "-" + number3 + "-" + number4;
    }

    @GetMapping("/districts")
    public String getDistricts(@RequestParam("provinceId") Integer provinceId, Model model) {
        model.addAttribute("districts", districtService.findAllByProvinceId(provinceId));
        return "client/fragments :: districtList";
    }

    @GetMapping("/wards")
    public String getWards(@RequestParam("districtId") Integer districtId, Model model) {
        model.addAttribute("wards", wardService.findAllByDistrictId(districtId));
        return "client/fragments :: wardList";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("isShowOtp", false);
        model.addAttribute("isShowNewPasswordForm", false);
        return "client/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("username") String username,
                                        Model model,
                                        HttpSession session) {
        Account account = accountService.findByUsername(username);
        if (account == null) {
            model.addAttribute("error", "Tên đăng nhập không tồn tại, vui lòng chọn tên đăng nhập khác!");
            model.addAttribute("isShowOtp", false);
            model.addAttribute("isShowNewPasswordForm", false);
            return "client/forgot-password";
        }

        Membership membership = memberShipService.findByAccountId(account.getId());
        String email = membership.getEmail();
        String code;

        try {
            code = accountService.generateCode();
            accountService.sendEmailOTP(email, code);
        } catch (Exception e) {
            model.addAttribute("error", "Không thể gửi email OTP. Vui lòng thử lại sau!");
            model.addAttribute("isShowOtp", false);
            model.addAttribute("isShowNewPasswordForm", false);
            return "client/forgot-password";
        }

        session.setAttribute("username", username);
        session.setAttribute("code", code);
        session.setAttribute("count", 4);

        model.addAttribute("isShowOtp", true);
        model.addAttribute("isShowNewPasswordForm", false);
        model.addAttribute("username", username);
        model.addAttribute("count", 4);
        return "client/forgot-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("username") String username,
                                       @RequestParam("code") String code,
                                       @RequestParam("otp") String otp,
                                       @RequestParam(value = "count", required = false) Integer count,
                                       Model model,
                                       HttpSession session) {
        String storedCode = (String) session.getAttribute("code");
        Integer storedCount = (Integer) session.getAttribute("count");

        if (storedCount == null) {
            storedCount = (count != null) ? count : 4;
        }

        if (otp.equals(storedCode)) {
            model.addAttribute("isShowOtp", false);
            model.addAttribute("isShowNewPasswordForm", true);
            model.addAttribute("username", username);
            model.addAttribute("accountDTO", new AccountDTO());
            return "client/forgot-password";
        } else {
            storedCount--;
            session.setAttribute("count", storedCount);

            if (storedCount <= 0) {
                session.removeAttribute("username");
                session.removeAttribute("code");
                session.removeAttribute("count");
                model.addAttribute("error", "Cập nhật mật khẩu không thành công, vui lòng thử lại");
                return "redirect:/";
            } else {
                model.addAttribute("isShowOtp", true);
                model.addAttribute("isShowNewPasswordForm", false);
                model.addAttribute("username", username);
                model.addAttribute("code", storedCode);
                model.addAttribute("count", storedCount);
                model.addAttribute("errorOTP", "Bạn còn " + storedCount + " lượt nhập");
                return "client/forgot-password";
            }
        }
    }

    @PostMapping("/update-password")
    public String processUpdatePassword(@RequestParam("username") String username,
                                        @Valid @ModelAttribute("accountDTO") ResetPasswordDTO resetPasswordDTO,
                                        BindingResult bindingResult,
                                        Model model,
                                        HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isShowOtp", false);
            model.addAttribute("isShowNewPasswordForm", true);
            model.addAttribute("username", username);
            return "client/forgot-password";
        }

        if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmPassword())) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            model.addAttribute("isShowOtp", false);
            model.addAttribute("isShowNewPasswordForm", true);
            model.addAttribute("username", username);
            return "client/forgot-password";
        }

        try {
            Account account = accountService.findByUsername(username);
            if (account == null) {
                model.addAttribute("error", "Tên đăng nhập không tồn tại!");
                model.addAttribute("isShowOtp", false);
                model.addAttribute("isShowNewPasswordForm", true);
                model.addAttribute("username", username);
                return "client/forgot-password";
            }
            accountService.changeResetPassword(account, resetPasswordDTO);
        } catch (Exception e) {
            model.addAttribute("error", "Không thể cập nhật mật khẩu. Vui lòng thử lại sau!");
            model.addAttribute("isShowOtp", false);
            model.addAttribute("isShowNewPasswordForm", true);
            model.addAttribute("username", username);
            return "client/forgot-password";
        }

        session.removeAttribute("username");
        session.removeAttribute("code");
        session.removeAttribute("count");

        return "redirect:/login?success=true";
    }


    @GetMapping("/deny")
    public String denied() {
        return "client/denied";
    }
}
