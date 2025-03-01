package org.example.cinema_fullstack.repositories;

import org.example.cinema_fullstack.dto.staff.EmployeeListDTO;
import org.example.cinema_fullstack.models.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(value = "select employee.id as id , employee.name as name , employee.card as card , employee.email as email , employee.phone as phone, ward.name as ward , district.name as district , province.name as province , employee.status\n" +
            "from `employee`\n" +
            "left join ward on employee.ward_id = ward.id\n" +
            "left join district on ward.district_id = district.id\n" +
            "left join province on province.id = district.province_id where status = 1", nativeQuery = true)
    Page<EmployeeListDTO> getPageAllStaff(Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "insert into employee(name, card , birthday ,ward_id , gender , email , phone , imageurl , status , account_id) values (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8 , ?9 , ?10)", nativeQuery = true)
    void createStaffDto(String name, String card, LocalDate birthday, int wardId, String gender, String email, String phone, String imageUrl , int status , Long accountId);

    @Transactional
    @Modifying
    @Query(value = "update employee set employee.status = 0 where employee.id=?1", nativeQuery = true)
    void deleteEmployeeById(Long id);

    @Transactional
    @Modifying
    @Query(value = "update employee as m set m.name = ?1,m.card = ?2,m.phone = ?3,m.email = ?4,m.birthday=?5,m.gender=?6,m.imageurl=?7,m.ward_id=?8 where m.id = ?9", nativeQuery = true)
    void updateEmployee(String name,String card,String phone,String email,LocalDate birthday,String gender,String imageURL,Integer wardId,Long id);

    @Query(value = "select * from `employee` where  account_id=?1 limit 1",
            nativeQuery = true)
    Employee findByAccountId(Long id);

}
