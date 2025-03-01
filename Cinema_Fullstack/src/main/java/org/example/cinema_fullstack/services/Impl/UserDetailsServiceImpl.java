package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.entity.Account;
import org.example.cinema_fullstack.repositories.AccountRepository;
import org.example.cinema_fullstack.repositories.AccountRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountRoleRepository accountRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User " + username + "was not found in the database");
        }
        List<String> roles = accountRoleRepository.findAllByUsername(username);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String role: roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        UserDetails userDetails = new User(username, account.getPassword(), grantedAuthorities);
        return userDetails;
    }
}
