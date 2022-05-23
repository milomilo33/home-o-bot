package com.robot.homeobot.repository;

import com.robot.homeobot.model.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealEstateRepository  extends JpaRepository<RealEstate, Long> {
}
