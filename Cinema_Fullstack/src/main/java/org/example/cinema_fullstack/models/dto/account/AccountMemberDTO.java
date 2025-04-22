package org.example.cinema_fullstack.models.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountMemberDTO {
    @NotBlank(message = "Tên đăng nhập không được để trống!")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Tên đăng nhập không hợp lệ. VD: phat1234!")
    @Size(min = 6, max = 45, message = "Tên đăng nhập phải từ 6 đến 45 ký tự!")
    String username;

    @NotBlank(message = "Mật khẩu không được để trống!")
    @Size(min = 6, max = 45, message = "Mật khẩu phải từ 6 đến 45 ký tự!")
    String password;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống!")
    String confirmPassword;

    @NotBlank(message = "Họ và tên không được để trống!")
    @Pattern(regexp = "^(\\s*)([\\p{Lu}]|[\\p{Ll}]){2,}((\\s*)(([\\p{Lu}]|[\\p{Ll}]){2,}))+(\\s*)$", message = "Họ và tên phải ít nhất 2 từ!")
    @Size(max = 45, message = "Họ và tên phải ít hơn 45 ký tự!")
    String name;

    String memberCode;

    @NotBlank(message = "Số CMND không được để trống!")
    @Pattern(regexp = "^[\\d]{9,12}$", message = "Số CMND chỉ được từ 9-12 chữ số!")
    String card;

    @NotBlank(message = "Số điện thoại không được để trống!")
    @Pattern(regexp = "^(0[3|5|7|8|9])+([0-9]{8})$", message = "Số điện thoại phải đúng định dạng: 09xxabcxyz!")
    String phone;

    @NotBlank(message = "Email không được để trống!")
    @Email(message = "Email phải đúng định dạng. VD: example@gmail.com!")
    @Size(max = 253, message = "Email phải ít hơn 253 ký tự!")
    String email;

    @NotNull(message = "Ngày sinh không được để trống!")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ!")
    LocalDate birthday;

    @NotBlank(message = "Giới tính không được để trống!")
    String gender;

    String imageURL;

    @NotNull(message = "Xã/Phường không được để trống!")
    Integer wardId;

    @NotNull(message = "Quận/Huyện không được để trống!")
    Integer districtId;

    @NotNull(message = "Tỉnh/Thành Phố không được để trống!")
    Integer provinceId;

    @AssertTrue(message = "Bạn phải chấp nhận điều khoản để hoàn thành việc đăng kí!")
    boolean confirm;
}
