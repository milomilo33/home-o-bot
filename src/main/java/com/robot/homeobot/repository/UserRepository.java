package com.robot.homeobot.repository;

import com.robot.homeobot.model.User;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

@JaversSpringDataAuditable
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
