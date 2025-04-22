package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.models.dto.account.AccountDTO;
import org.example.cinema_fullstack.models.dto.account.ResetPasswordDTO;
import org.example.cinema_fullstack.models.entity.Account;
import org.example.cinema_fullstack.repositories.AccountRepository;
import org.example.cinema_fullstack.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void createAccount(int isEnable, String password, String username) {
        accountRepository.createAccount(isEnable , password , username);
    }

    @Override
    public Long getIdAccountByUsername(String username) {
        return accountRepository.getIdAccountByUsername(username);
    }

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    @Override
    public void createAccount(String username, String password) {
        accountRepository.createAccount(username,passwordEncoder.encode(password));
    }

    @Override
    public boolean checkChangePassword(Account account, AccountDTO accountDTO) {
        if (passwordEncoder.matches(accountDTO.getOldPassword(), account.getPassword())) {
                return true;
        }
        return false;
    }

    @Override
    public boolean checkNewPwEqualOldPw(Account account, AccountDTO accountDTO) {
        if (passwordEncoder.matches(accountDTO.getNewPassword(), account.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public void changePassword(Account account,AccountDTO accountDTO) {
            accountRepository.changePassword(passwordEncoder.encode(accountDTO.getNewPassword()), account.getUsername());
    }

    @Override
    public void changeResetPassword(Account account, ResetPasswordDTO resetPasswordDTO) {
        accountRepository.changePassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()), account.getUsername());
    }

    @Override
    public String generateCode() {
        return "" + (new Random().nextInt(900000) + 100000);
    }

    @Override
    public void sendEmailOTP(String email, String code) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Email lấy lại mật khẩu từ Cinema");
            String htmlMsg = "<h3>Your code is <i style='color: green'>" + code + "</i></h3>" +
                    "<p style='color: red; font-size: 25px;'>Cinema</p>" +
                    "<p>Chào bạn!</p>" +
                    "<p>Rạp chiếu phim Cinema gửi bạn mã code OTP bên dưới để đổi lại mật khẩu.</p>" +
                    "<p>Mã CODE bao gồm 6 số: <b>" + code + "</b></p>" +
                    "<p>Thanks and regards!</p>";
            helper.setText(htmlMsg, true);

            emailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email OTP. Vui lòng thử lại sau!", e);
        }
    }

    @Override
    public void changePasswordByForgot(String password, Account account) {
        accountRepository.changePassword(passwordEncoder.encode("12345678"),account.getUsername());
    }
}
