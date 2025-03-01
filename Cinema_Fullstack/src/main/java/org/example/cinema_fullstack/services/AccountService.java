package org.example.cinema_fullstack.services;

import org.example.cinema_fullstack.models.dto.account.AccountDTO;
import org.example.cinema_fullstack.models.entity.Account;

public interface AccountService {

    void createAccount(int isEnable , String  password , String username);

    Long getIdAccountByUsername(String username);
    Account findByUsername(String username);
    void createAccount(String username, String password);
    boolean checkChangePassword(Account account, AccountDTO accountDTO);
    boolean checkNewPwEqualOldPw(Account account,AccountDTO accountDTO);
    void changePassword(Account account,AccountDTO accountDTO);
    String generateCode();
    void sendEmailOTP(String email, String code);
    void changePasswordByForgot(String password,Account account);
}
