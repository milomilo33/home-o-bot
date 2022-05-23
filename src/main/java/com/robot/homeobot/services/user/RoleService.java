package com.robot.homeobot.services.user;
import com.robot.homeobot.model.Role;

import java.util.List;



public interface RoleService {
    Role findById(Long id);
    List<Role> findByName(String name);

    List<Role> getAllRoles();
}
