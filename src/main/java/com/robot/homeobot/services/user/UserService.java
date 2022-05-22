package com.robot.homeobot.services.user;

import com.robot.homeobot.dto.UserRequest;
import com.robot.homeobot.model.User;

import java.util.List;



public interface UserService {
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll ();
    User save(UserRequest userRequest);
}
