package com.robot.homeobot.repository;

import com.robot.homeobot.model.RealEstate;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

@JaversSpringDataAuditable
public interface RealEstateRepository  extends JpaRepository<RealEstate, Long> {
}
