package org.example.cinema_fullstack.repositories;

import org.example.cinema_fullstack.models.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "select role.id from role where role.name = ?1 limit 1", nativeQuery = true)
    Long getIdRoleIdByRoleName(String roleName);


}
