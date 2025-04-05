package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.dto.account.AccountMemberDTO;
import org.example.cinema_fullstack.models.dto.membership.MembershipUpdateDTO;
import org.example.cinema_fullstack.models.entity.Account;
import org.example.cinema_fullstack.models.entity.Membership;
import org.example.cinema_fullstack.repositories.AccountRepository;
import org.example.cinema_fullstack.repositories.MemberShipRepository;
import org.example.cinema_fullstack.services.MemberShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberShipServiceImpl implements MemberShipService {
    @Autowired
    MemberShipRepository memberShipRepository;
    @Autowired
    AccountRepository accountRepository;
    @Override
    public Membership findByEmail(String email) {
        return memberShipRepository.findByEmail(email);
    }

    @Override
    public Membership findByPhone(String phone) {
        return memberShipRepository.findByPhone(phone);
    }

    @Override
    public Membership findByCard(String card) {
        return memberShipRepository.findByCard(card);
    }

    @Override
    public Membership findById(long id) {
        return memberShipRepository.findById(id);
    }

    @Override
    public Membership findByUsername(String username) {
        return memberShipRepository.findByUsername(username);
    }

    @Override
    public Membership findByAccountId(long accountId) {
        return memberShipRepository.findByAccountId(accountId);
    }

    @Override
    public void createMembership(AccountMemberDTO membership) {
        Account account = accountRepository.findAccountByUsername(membership.getUsername());
        memberShipRepository.createMembership(
                membership.getMemberCode()
                ,membership.getName().replaceAll("\\s{2,}", " ").trim()
                ,membership.getCard()
                ,membership.getPhone()
                ,membership.getEmail()
                ,membership.getBirthday()
                ,membership.getGender()
                ,membership.getImageURL()
                ,membership.getWardId()
                ,account.getId());
    }

    @Override
    @Transactional
    public void updateMembership(MembershipUpdateDTO membership) {
        System.out.println("Updating membership: " + membership);
        memberShipRepository.updateMembership(
                membership.getMemberCode()
                ,membership.getName().replaceAll("\\s{2,}", " ").trim()
                ,membership.getCard()
                ,membership.getPhone()
                ,membership.getEmail()
                ,membership.getBirthday()
                ,membership.getGender()
                ,membership.getImageURL()
                ,membership.getWardId()
                ,membership.getId());

    }

    @Override
    public void createMembershipSocial(Membership membership) {
        memberShipRepository.createMembershipWithSocial(membership.getEmail(),membership.getAccount().getId(),this.generateMemberCode());
    }
    public String generateMemberCode() {
        int number1 = (int) Math.floor(1000 + Math.random() * 9000);
        int number2 = (int) Math.floor(1000 + Math.random() * 9000);
        int number3 = (int) Math.floor(1000 + Math.random() * 9000);
        int number4 = (int) Math.floor(1000 + Math.random() * 9000);
        return number1+"-"+number2+"-"+number3+"-"+number4;
    }
}
