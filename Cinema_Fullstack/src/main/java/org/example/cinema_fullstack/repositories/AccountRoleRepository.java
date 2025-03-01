package org.example.cinema_fullstack.repositories;

import org.example.cinema_fullstack.models.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {
    @Transactional
    @Modifying
    @Query(value = "insert into account_role(account_id, role_id) values (?1, ?2)", nativeQuery = true)
    void createAccountRole(long accountId, long roleId);

    List<AccountRole> findAllByAccountUsername(String username);

    @Query("select accountRole.role.name from AccountRole accountRole where accountRole.account.username = :username")
    List<String> findAllByUsername(String username);
}
