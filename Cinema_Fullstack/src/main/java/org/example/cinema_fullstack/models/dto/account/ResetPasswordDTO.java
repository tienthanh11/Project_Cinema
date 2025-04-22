package org.example.cinema_fullstack.models.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDTO {
    @NotBlank(message = "Mật khẩu mới không được để trống!")
    @Size(min = 6, max = 45, message = "Mật khẩu mới phải chứa từ 6 đến 45 kí tự!")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Mật khẩu mới không được chứa kí tự đặc biệt!")
    private String newPassword;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống!")
    @Size(min = 6, max = 45, message = "Xác nhận mật khẩu phải chứa từ 6 đến 45 kí tự!")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Xác nhận mật khẩu không được chứa kí tự đặc biệt!")
    private String confirmPassword;
}