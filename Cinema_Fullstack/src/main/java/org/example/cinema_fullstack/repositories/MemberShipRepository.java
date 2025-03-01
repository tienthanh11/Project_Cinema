package org.example.cinema_fullstack.repositories;

import org.example.cinema_fullstack.models.dto.membership.MembershipViewDTO;
import org.example.cinema_fullstack.models.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
public interface MemberShipRepository extends JpaRepository<Membership, Long> {
    //    PhatDT
    Membership findByAccountId(long accountId);

    @Query(value = "select * from `membership` where card =?1 limit 1",
            nativeQuery = true)
    Membership findByCard(String card);
    @Query(value = "select * from `membership` where email =?1 limit 1",
            nativeQuery = true)
    Membership findByEmail(String email);
    @Query(value = "select * from `membership` where phone =?1 limit 1",
            nativeQuery = true)
    Membership findByPhone(String phone);
    @Query(value = "select * from `membership` where id =?1 limit 1",
            nativeQuery = true)
    Membership findById(long id);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO membership (member_code,name,card,phone,email,birthday,gender,imageurl,ward_id,account_id) VALUE (?1,?2,?3,?4,?5,?6,?7,?8,?9,?10)",
            nativeQuery = true)
    void createMembership(String memberCode, String name, String card, String phone, String email, LocalDate birthday, String gender, String imageurl, Integer wardId, Long accountId);

    @Transactional
    @Modifying
    @Query(value = "update membership as m set m.name = ?1,m.card = ?2,m.phone = ?3,m.email = ?4,m.birthday=?5,m.gender=?6,m.imageurl=?7,m.ward_id=?8 where m.id = ?9 ",
            nativeQuery = true)
    void updateMembership(String name, String card, String phone, String email, LocalDate birthday, String gender, String imageURL, Integer wardId, Long id);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO membership (email,account_id,ward_id,member_code) value (?1,?2,1,?3) ", nativeQuery = true)
    void createMembershipWithSocial(String email, Long accountId, String memberCode);

    @Modifying
    @Query(value = "select * from membership where account_id = ?1", nativeQuery = true)
    MembershipViewDTO findMembershipByAccounId(Long accountId);

}
