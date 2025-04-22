package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.dto.account.AccountMemberDTO;
import org.example.cinema_fullstack.models.dto.membership.MembershipUpdateDTO;
import org.example.cinema_fullstack.models.entity.Account;
import org.example.cinema_fullstack.models.entity.Membership;
import org.example.cinema_fullstack.models.entity.Ward;
import org.example.cinema_fullstack.repositories.AccountRepository;
import org.example.cinema_fullstack.repositories.MemberShipRepository;
import org.example.cinema_fullstack.repositories.WardRepository;
import org.example.cinema_fullstack.services.MemberShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MemberShipServiceImpl implements MemberShipService {

    @Autowired
    MemberShipRepository memberShipRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    WardRepository wardRepository;

    @Override
    public List<Membership> findAll() {
        return memberShipRepository.findAll();
    }

    @Override
    public Page<Membership> findAllWithPagination(Pageable pageable) {
        return memberShipRepository.findAllWithPaginationFilteredByRole(pageable);
    }

    @Override
    public Page<Membership> searchByName(String name, Pageable pageable) {
        return memberShipRepository.searchByNameFilteredByRole(name, pageable);
    }

    @Override
    public Membership findById(Long id) {
        return memberShipRepository.findById(id).orElse(null);
    }

    @Override
    public Membership getMembershipDetail(Long id) {
        return memberShipRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Membership updateMembership(Long id, MembershipUpdateDTO membershipUpdateDTO) {
        Optional<Membership> optionalMembership = memberShipRepository.findById(id);
        if (optionalMembership.isPresent()) {
            Membership membership = optionalMembership.get();
            if (membershipUpdateDTO.getMemberCode() != null) {
                membership.setMemberCode(membershipUpdateDTO.getMemberCode());
            }
            if (membershipUpdateDTO.getName() != null) {
                membership.setName(membershipUpdateDTO.getName());
            }
            if (membershipUpdateDTO.getCard() != null) {
                membership.setCard(membershipUpdateDTO.getCard());
            }
            if (membershipUpdateDTO.getPhone() != null) {
                membership.setPhone(membershipUpdateDTO.getPhone());
            }
            if (membershipUpdateDTO.getEmail() != null) {
                membership.setEmail(membershipUpdateDTO.getEmail());
            }
            if (membershipUpdateDTO.getBirthday() != null) {
                membership.setBirthday(membershipUpdateDTO.getBirthday());
            }
            if (membershipUpdateDTO.getGender() != null) {
                membership.setGender(membershipUpdateDTO.getGender());
            }
            if (membershipUpdateDTO.getImageURL() != null) {
                membership.setImageURL(membershipUpdateDTO.getImageURL());
            }
            if (membershipUpdateDTO.getWardId() != null) {
                Optional<Ward> ward = wardRepository.findById(membershipUpdateDTO.getWardId());
                ward.ifPresent(membership::setWard);
            }
            return memberShipRepository.save(membership);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteMembership(Long id) {
        Optional<Membership> membershipOpt = memberShipRepository.findById(id);
        if (membershipOpt.isPresent()) {
            Membership membership = membershipOpt.get();
            membership.setAccount(null);
            memberShipRepository.save(membership);
            memberShipRepository.deleteById(id);
            return true;
        }
        return false;
    }

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
