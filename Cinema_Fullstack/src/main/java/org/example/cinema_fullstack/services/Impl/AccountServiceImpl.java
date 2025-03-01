//package org.example.cinema_fullstack.services.Impl;
//
//
//import com.a0720i1.cinema_project.models.dto.account.AccountDTO;
//import com.a0720i1.cinema_project.models.entity.Account;
//import com.a0720i1.cinema_project.repositories.AccountRepository;
//import com.a0720i1.cinema_project.services.AccountService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Random;
//
//@Service
//public class AccountServiceImpl implements AccountService {
//    @Autowired
//    AccountRepository accountRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private JavaMailSender emailSender;
//
//    @Override
//    public void createAccount(int isEnable, String password, String username) {
//        accountRepository.createAccount(isEnable , password , username);
//    }
//
//    @Override
//    public Long getIdAccountByUsername(String username) {
//        return accountRepository.getIdAccountByUsername(username);
//    }
//
//    @Override
//    public Account findByUsername(String username) {
//        return accountRepository.findAccountByUsername(username);
//    }
//
//    @Override
//    public void createAccount(String username, String password) {
//        accountRepository.createAccount(username,passwordEncoder.encode(password));
//    }
//
//    @Override
//    public boolean checkChangePassword(Account account,AccountDTO accountDTO) {
//        if (passwordEncoder.matches(accountDTO.getOldPassword(), account.getPassword())) {
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean checkNewPwEqualOldPw(Account account, AccountDTO accountDTO) {
//        if (passwordEncoder.matches(accountDTO.getNewPassword(), account.getPassword())) {
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void changePassword(Account account,AccountDTO accountDTO) {
//            accountRepository.changePassword(passwordEncoder.encode(accountDTO.getNewPassword()), account.getUsername());
//    }
//
//    @Override
//    public String generateCode() {
//        return "" + (new Random().nextInt(900000) + 100000);
//    }
//
//    @Override
//    public void sendEmailOTP(String email, String code) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        String htmlMsg = "<h3>Your code is <i style='color: green'>" + code + "<i></h3>" +
//                "<p style='color: red; font-size: 25px;'>" +
//                "A0720I1 <p>";
//        message.setSubject("Email lấy lại mật khẩu từ A0720I1");
//        message.setText("Chào bạn!\n"
//                + "Rạp chiếu phim A0720I1 gửi bạn mã code OTP bên dưới để đổi lại mật khẩu.\n"
//                + "Mã CODE bao gồm 6 số : " + code + "\n\n"
//                + "Thanks and regards!");
//        this.emailSender.send(message);
//    }
//
//    @Override
//    public void changePasswordByForgot(String password, Account account) {
//        accountRepository.changePassword(passwordEncoder.encode("12345678"),account.getUsername());
//    }
//}
