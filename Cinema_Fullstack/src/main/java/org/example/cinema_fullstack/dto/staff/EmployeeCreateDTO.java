package org.example.cinema_fullstack.dto.staff;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeCreateDTO {
    String username;
    String password;
    String name;
    String card;
    LocalDate birthday;
    Integer wardId;
    String gender;
    String email;
    String phone;
    String imageUrl;
}
