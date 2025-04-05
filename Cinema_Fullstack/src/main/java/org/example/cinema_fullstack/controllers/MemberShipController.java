package org.example.cinema_fullstack.controllers;

import org.example.cinema_fullstack.models.dto.account.AccountDTO;
import org.example.cinema_fullstack.models.dto.membership.MembershipUpdateDTO;
import org.example.cinema_fullstack.models.dto.ticket.TicketDTO;
import org.example.cinema_fullstack.models.entity.*;
import org.example.cinema_fullstack.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberShipController {

    @Autowired
    private MemberShipService membershipService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private WardService wardService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthService authService;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @GetMapping("/manager/info/{username}")
    public String showUpdateForm(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        Membership membership = membershipService.findByUsername(username);
        if (membership == null) {
            return "redirect:/login?error=userNotFound";
        }

        MembershipUpdateDTO membershipUpdate = convertToDTO(membership);

        model.addAttribute("membership", membership);
        model.addAttribute("membershipUpdate", membershipUpdate);
        model.addAttribute("provinces", provinceService.findAll());
        model.addAttribute("districts", districtService.findAllByProvinceId(membership.getWard().getDistrict().getProvince().getId()));
        model.addAttribute("wards", wardService.findAllByDistrictId(membership.getWard().getDistrict().getId()));
        model.addAttribute("defaultImage", "/uploads/default.jpg");

        authService.addAuthAttributes(model, authentication);
        return "client/member/update-info";
    }

    @PostMapping("/manager/info/update")
    public String updateMembership(@Valid @ModelAttribute("membershipUpdate") MembershipUpdateDTO membershipUpdate,
                                   BindingResult bindingResult,
                                   @RequestParam("imageFile") MultipartFile imageFile,
                                   Model model,
                                   Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        Membership membership = membershipService.findByUsername(username);
        if (membership == null) {
            return "redirect:/login?error=userNotFound";
        }

        model.addAttribute("membership", membership);

        // Load provinces, districts, wards for form
        List<Province> provinces = provinceService.findAll();
        List<District> districts = membershipUpdate.getProvinceId() != null
                ? districtService.findAllByProvinceId(membershipUpdate.getProvinceId())
                : districtService.findAllByProvinceId(membership.getWard().getDistrict().getProvince().getId());
        List<Ward> wards = membershipUpdate.getDistrictId() != null
                ? wardService.findAllByDistrictId(membershipUpdate.getDistrictId())
                : wardService.findAllByDistrictId(membership.getWard().getDistrict().getId());

        model.addAttribute("provinces", provinces);
        model.addAttribute("districts", districts);
        model.addAttribute("wards", wards);
        model.addAttribute("defaultImage", "/uploads/default.jpg");

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.toString()));
            return "client/member/update-info";
        }

        try {
            // Handle file upload
            if (!imageFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, imageFile.getBytes());
                membershipUpdate.setImageURL("/uploads/" + fileName);
            } else {
                membershipUpdate.setImageURL(membership.getImageURL());
            }

            // Update membership
            membershipService.updateMembership(membershipUpdate);

            Membership updateMembership = membershipService.findByUsername(username);
            model.addAttribute("membership", updateMembership);
            model.addAttribute("membershipUpdate", convertToDTO(updateMembership));
            model.addAttribute("successMessage", "Cập nhật thông tin thành công");

            authService.addAuthAttributes(model, authentication);
            return "client/member/update-info";
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Lỗi khi tải ảnh lên: " + e.getMessage());
            return "client/member/update-info";
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                if (e.getMessage().contains("card")) {
                    model.addAttribute("existCard", "Số CMND đã tồn tại!");
                } else if (e.getMessage().contains("phone")) {
                    model.addAttribute("existPhone", "Số điện thoại đã tồn tại!");
                }
            } else {
                model.addAttribute("errorMessage", "Cập nhật thất bại: " + e.getMessage());
            }

            return "client/member/update-info";
        }
    }

    private MembershipUpdateDTO convertToDTO(Membership membership) {
        MembershipUpdateDTO dto = new MembershipUpdateDTO();
        dto.setId(membership.getId());
        dto.setMemberCode(membership.getMemberCode());
        dto.setName(membership.getName());
        dto.setCard(membership.getCard());
        dto.setPhone(membership.getPhone());
        dto.setEmail(membership.getEmail());

        // Chuyển đổi LocalDate thành String để đảm bảo đúng định dạng
        if (membership.getBirthday() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            dto.setBirthday(membership.getBirthday()); // Không cần parse lại
        }

        dto.setGender(membership.getGender());
        dto.setImageURL(membership.getImageURL());
        dto.setWardId(membership.getWard().getId());
        dto.setDistrictId(membership.getWard().getDistrict().getId());
        dto.setProvinceId(membership.getWard().getDistrict().getProvince().getId());
        return dto;
    }

    @GetMapping("/manager/ticket")
    public String showTicketManagementPage(@RequestParam(defaultValue = "0") int page,
                                           Model model,
                                           Authentication authentication) {
        String username = authentication.getName();
        Membership membership = membershipService.findByUsername(username);
        if (membership == null) {
            model.addAttribute("error", "Vui lòng đăng nhập để xem vé của bạn");
            return "redirect:/login?redirect=/member/manager/ticket";
        }

        Pageable pageable = PageRequest.of(page, 10);
        Page<TicketDTO> ticketPage = ticketService.findTicketOfMembership(pageable, membership.getId());

        model.addAttribute("tickets", ticketPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ticketPage.getTotalPages());
        model.addAttribute("hasTickets", !ticketPage.getContent().isEmpty());

        authService.addAuthAttributes(model, authentication);
        return "client/member/manager-ticket";
    }

    @GetMapping("/manager/password")
    public String showChangePasswordForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        Account account = accountService.findByUsername(username);
        Membership membership = membershipService.findByUsername(username);

        if (account == null || membership == null) {
            return "redirect:/login?redirect=/member/manager/password";
        }

        model.addAttribute("username", account.getUsername());
        model.addAttribute("memberCode", membership.getMemberCode());
        model.addAttribute("accountDTO", new AccountDTO());

        authService.addAuthAttributes(model, authentication);
        return "client/member/change-password";
    }

    @PostMapping("/manager/change-password")
    public String processChangePassword(@Valid @ModelAttribute("accountDTO") AccountDTO accountDTO,
                                        BindingResult bindingResult,
                                        Authentication authentication,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Account account = accountService.findByUsername(username);
        Membership membership = membershipService.findByUsername(username);

        if (account == null || membership == null) {
            return "redirect:/login?redirect=/member/manager/password";
        }

        model.addAttribute("username", account.getUsername());
        model.addAttribute("memberCode", membership.getMemberCode());

        if (bindingResult.hasErrors()) {
            return "client/member/change-password";
        }

        if (!accountService.checkChangePassword(account, accountDTO)) {
            bindingResult.rejectValue("oldPassword", "failPassword", "Mật khẩu cũ không đúng!");
            return "client/member/change-password";
        }

        if (accountService.checkNewPwEqualOldPw(account, accountDTO)) {
            bindingResult.rejectValue("newPassword", "equalPassword", "Mật khẩu mới không được trùng mật khẩu cũ!");
            return "client/member/change-password";
        }

        if (!accountDTO.getNewPassword().equals(accountDTO.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "notMatchPassword", "Xác nhận mật khẩu phải giống mật khẩu mới!");
            return "client/member/change-password";
        }

        try {
            accountService.changePassword(account, accountDTO);
            redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thay đổi mật khẩu thất bại: " + e.getMessage());
            return "redirect:/member/change-password";
        }
    }
}
