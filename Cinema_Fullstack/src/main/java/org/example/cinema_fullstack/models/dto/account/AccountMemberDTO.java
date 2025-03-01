package org.example.cinema_fullstack.models.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountMemberDTO {
    String username;
    String password;
    String confirmPassword;
    String name;
    String memberCode;
    String card;
    String phone;
    String email;
    LocalDate birthday;
    String gender;
    String imageURL;
    Integer wardId;
}
