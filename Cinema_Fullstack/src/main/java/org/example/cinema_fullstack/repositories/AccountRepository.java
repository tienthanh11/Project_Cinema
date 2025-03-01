package org.example.cinema_fullstack.repositories;

import org.example.cinema_fullstack.models.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Modifying
    @Query(value = "INSERT INTO `movie`.`account` (`is_enable`, `password`, `username`) VALUES (?1, ?2 , ?3)",
            nativeQuery = true)
    void createAccount(int isEnable , String  password , String username);

    @Query(value = "select account.id from account where account.username = ?1 limit 1", nativeQuery = true)
    Long getIdAccountByUsername(String username);

    Account findAccountByUsername(String username);

    @Transactional
    @Modifying
    @Query(value = "insert into account(username, password, is_enable) values (?1, ?2, 1)", nativeQuery = true)
    void createAccount(String username, String password);

    @Transactional
    @Modifying
    @Query(value = "UPDATE account SET password = ?1 WHERE username = ?2", nativeQuery = true)
    void changePassword(String encodePw, String username);
}


