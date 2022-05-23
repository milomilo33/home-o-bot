package com.robot.homeobot.repositories;

import com.robot.homeobot.models.Revocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;

public interface RevocationRepository extends JpaRepository<Revocation, Long> {
    Revocation findByCN(String CN);
}