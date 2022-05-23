package com.robot.homeobot.services.realestate;

import com.robot.homeobot.dto.UserRequest;
import com.robot.homeobot.dtos.RealEstateDTO;
import com.robot.homeobot.model.RealEstate;
import com.robot.homeobot.repository.RealEstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealEstateServiceImpl implements RealEstateService {

    @Autowired
    private RealEstateRepository realEstateRepository;
    @Override
    public RealEstate findById(Long id) {
        return realEstateRepository.findById(id).orElseThrow();
    }

    @Override
    public List<RealEstate> findAll() {
        return realEstateRepository.findAll();
    }

    @Override
    public RealEstate save(RealEstateDTO realEstateDTO) {
        RealEstate realEstate = new RealEstate();
        realEstate.setName(realEstate.getName());
        return realEstateRepository.save(realEstate);
    }
}
