package org.example.cinema_fullstack.services.Impl;

import org.example.cinema_fullstack.repositories.RoleRepository;
import org.example.cinema_fullstack.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Override
    public Long getIdRoleIdByRoleName(String roleName) {
        return roleRepository.getIdRoleIdByRoleName(roleName) ;
    }
}
