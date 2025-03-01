package org.example.cinema_fullstack.services;

import org.springframework.stereotype.Service;

@Service
public interface RoleService {

    Long getIdRoleIdByRoleName(String roleName);
}
