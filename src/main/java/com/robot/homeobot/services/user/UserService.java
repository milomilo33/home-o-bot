package com.robot.homeobot.services.user;

import com.robot.homeobot.dto.UserRequest;
import com.robot.homeobot.dtos.DeviceDTO;
import com.robot.homeobot.model.Device;
import com.robot.homeobot.model.Role;
import com.robot.homeobot.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;



public interface UserService {
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll ();
    User save(UserRequest userRequest);

    User updateUser(Long userId, UserRequest userRequest);

    void deleteUser(Long userId);

    List<Role> getAllRoles();

    List<DeviceDTO> getAllDevicesForOwnerOrRenter();
}
