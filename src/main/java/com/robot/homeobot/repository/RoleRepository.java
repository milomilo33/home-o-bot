package com.robot.homeobot.repository;

import java.util.List;

import com.robot.homeobot.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;



public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByName(String name);
}
