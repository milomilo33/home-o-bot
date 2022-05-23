package com.robot.homeobot.repository;


import com.robot.homeobot.model.JwtBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;



public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist,String> {


    JwtBlacklist findByTokenEquals(String token);

}