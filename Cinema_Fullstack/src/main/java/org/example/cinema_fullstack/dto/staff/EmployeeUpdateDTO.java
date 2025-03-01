package org.example.cinema_fullstack.dto.staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateDTO {
    Long id;
    String name;
    String card;
    String phone;
    String email;
    LocalDate birthday;
    String gender;
    String imageURL;
    Integer wardId;
}
