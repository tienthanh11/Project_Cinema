package org.example.cinema_fullstack.services;

import org.example.cinema_fullstack.dto.staff.EmployeeListDTO;
import org.example.cinema_fullstack.dto.staff.EmployeeUpdateDTO;
import org.example.cinema_fullstack.models.entity.District;
import org.example.cinema_fullstack.models.entity.Employee;
import org.example.cinema_fullstack.models.entity.Province;
import org.example.cinema_fullstack.models.entity.Ward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;


public interface EmployeeService {

    Page<EmployeeListDTO> getPageAllStaff(Pageable pageable);

    void createStaffDto(String name, String card, LocalDate birthday, int wardId, String gender, String email, String phone, String imageUrl , int status , Long accountId);

    List<Ward> findAllByDistrictId(int districtId);
    List<District> findAllByProvinceId(int provinceId);
    List<Province> findAll();

    Employee findById(Long id);
    void deleteById(Long id);
    void update(EmployeeUpdateDTO employee);



}
