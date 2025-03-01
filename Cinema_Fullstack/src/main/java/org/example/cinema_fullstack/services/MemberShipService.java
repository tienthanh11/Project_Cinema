package org.example.cinema_fullstack.services;

import org.example.cinema_fullstack.models.dto.account.AccountMemberDTO;
import org.example.cinema_fullstack.models.dto.membership.MembershipUpdateDTO;
import org.example.cinema_fullstack.models.entity.Membership;

public interface MemberShipService {
    Membership findByEmail(String email);
    Membership findByPhone(String phone);
    Membership findByCard(String card);
    Membership findById(long id);
    Membership findByAccountId(long accountId);
    void createMembership(AccountMemberDTO membership);
    void updateMembership(MembershipUpdateDTO membership);
    void createMembershipSocial(Membership membership);
}
