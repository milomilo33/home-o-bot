package com.robot.homeobot.controller;

import com.robot.homeobot.dtos.RealEstateDTO;
import com.robot.homeobot.model.RealEstate;
import com.robot.homeobot.model.User;
import com.robot.homeobot.services.realestate.RealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/real-estate", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class RealEstateController {

    @Autowired
    private RealEstateService realEstateService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CREATE_REAL_ESTATE')")
    public RealEstate newRealEstate(RealEstateDTO realEstateDTO){
        return this.realEstateService.save(realEstateDTO);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('READ_REAL_ESTATES')")
    public List<RealEstate> loadAll() {
        return this.realEstateService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_REAL_ESTATE')")
    public RealEstate loadById(@PathVariable Long id) {
        return this.realEstateService.findById(id);
    }

}
