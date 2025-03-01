package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.entity.AccountRole;
import org.example.cinema_fullstack.repositories.AccountRoleRepository;
import org.example.cinema_fullstack.services.AccountRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountRoleServiceImpl implements AccountRoleService {

    @Autowired
    AccountRoleRepository accountRoleRepository;

    @Override
    public void createAccountRole(long accountId, long roleId) {
        accountRoleRepository.createAccountRole(accountId, roleId);
    }

    @Override
    public List<AccountRole> findAllByAccountUsername(String username) {
        return accountRoleRepository.findAllByAccountUsername(username);
    }
}
