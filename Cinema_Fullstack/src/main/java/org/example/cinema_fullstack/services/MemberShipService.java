package org.example.cinema_fullstack.services;

import org.example.cinema_fullstack.models.dto.account.AccountMemberDTO;
import org.example.cinema_fullstack.models.dto.membership.MembershipUpdateDTO;
import org.example.cinema_fullstack.models.entity.Membership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberShipService {

    List<Membership> findAll();
    Page<Membership> findAllWithPagination(Pageable pageable);
    Page<Membership> searchByName(String name, Pageable pageable);
    Membership findById(Long id);
    Membership getMembershipDetail(Long id);
    Membership updateMembership(Long id, MembershipUpdateDTO membershipUpdateDTO);
    boolean deleteMembership(Long id);
    Membership findByEmail(String email);
    Membership findByPhone(String phone);
    Membership findByCard(String card);
    Membership findById(long id);
    Membership findByUsername(String username);
    Membership findByAccountId(long accountId);
    void createMembership(AccountMemberDTO membership);
    void updateMembership(MembershipUpdateDTO membership);
    void createMembershipSocial(Membership membership);
}
