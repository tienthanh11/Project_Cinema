package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.dto.staff.EmployeeListDTO;
import org.example.cinema_fullstack.dto.staff.EmployeeUpdateDTO;
import org.example.cinema_fullstack.models.entity.District;
import org.example.cinema_fullstack.models.entity.Employee;
import org.example.cinema_fullstack.models.entity.Province;
import org.example.cinema_fullstack.models.entity.Ward;
import org.example.cinema_fullstack.repositories.DistrictRepository;
import org.example.cinema_fullstack.repositories.EmployeeRepository;
import org.example.cinema_fullstack.repositories.ProvinceRepository;
import org.example.cinema_fullstack.repositories.WardRepository;
import org.example.cinema_fullstack.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public Page<EmployeeListDTO> getPageAllStaff(Pageable pageable) {
        return employeeRepository.getPageAllStaff(pageable);
    }

    @Override
    public void createStaffDto(String name, String card, LocalDate birthday, int wardId, String gender, String email, String phone, String imageUrl , int status , Long accountId) {
        employeeRepository.createStaffDto(name, card , birthday , wardId , gender , email , phone , imageUrl , status , accountId);
    }

    @Override
    public List<Ward> findAllByDistrictId(int districtId) {
        return wardRepository.findAllByDistrictId(districtId);
    }

    @Override
    public List<District> findAllByProvinceId(int provinceId) {
        return districtRepository.findAllByProvinceId(provinceId);
    }

    @Override
    public List<Province> findAll() {
        return provinceRepository.findAll();
    }

    @Override
    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteEmployeeById(id);
    }

    @Override
    public void update(EmployeeUpdateDTO employee) {
        employeeRepository.updateEmployee(employee.getName()
                ,employee.getCard().replaceAll("\\s{2,}", " ").trim()
                ,employee.getPhone()
                ,employee.getEmail()
                ,employee.getBirthday()
                ,employee.getGender()
                ,employee.getImageURL()
                ,employee.getWardId()
                ,employee.getId());
    }


}
