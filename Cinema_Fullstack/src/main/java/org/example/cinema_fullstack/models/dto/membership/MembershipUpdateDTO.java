package org.example.cinema_fullstack.models.dto.membership;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipUpdateDTO {
    Long id;

    @NotNull(message = "MemberCode không được để trống!")
    String memberCode;

    @NotBlank(message = "Họ và tên không được để trống!")
    @Size(max = 45, message = "Họ và tên phải ít hơn 45 ký tự!")
    @Pattern(regexp = "^(\\s*)([\\p{Lu}]|[\\p{Ll}]){2,}((\\s*)(([\\p{Lu}]|[\\p{Ll}]){2,}))+(\\s*)$", message = "Họ và tên phải có ít nhất 2 từ!")
    private String name;

    @NotBlank(message = "Số CMND không được để trống!")
    @Pattern(regexp = "^[\\d]{9,12}$", message = "Số CMND chỉ được từ 9-12 chữ số!")
    private String card;

    @NotBlank(message = "Số điện thoại không được để trống!")
    @Pattern(regexp = "^(0[3|5|7|8|9])+([0-9]{8})$", message = "Số điện thoại phải đúng định dạng: 09xxabcxyz!")
    private String phone;

    @NotNull(message = "Email không được để trống!")
    @Email(message = "Email không hợp lệ!")
    private String email;

    @NotNull(message = "Ngày sinh không được để trống!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotBlank(message = "Giới tính không được để trống!")
    private String gender;

    private String imageURL;

    @NotNull(message = "Xã/Phường không được để trống!")
    private Integer wardId;

    @NotNull(message = "Quận/Huyện không được để trống!")
    private Integer districtId;

    @NotNull(message = "Tỉnh/Thành Phố không được để trống!")
    private Integer provinceId;
}
