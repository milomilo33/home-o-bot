package com.robot.homeobot.services.user;

import java.util.List;

import com.robot.homeobot.model.Role;
import com.robot.homeobot.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findById(Long id) {
        Role auth = this.roleRepository.getOne(id);
        return auth;
    }

    @Override
    public List<Role> findByName(String name) {
        List<Role> roles = this.roleRepository.findByName(name);
        return roles;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }


}
