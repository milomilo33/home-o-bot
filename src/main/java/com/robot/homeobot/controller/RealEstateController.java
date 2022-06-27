package com.robot.homeobot.controller;

import com.robot.homeobot.dtos.ChangeRealEstateOwnersDTO;
import com.robot.homeobot.dtos.ChangeRealEstateRentersDTO;
import com.robot.homeobot.dtos.RealEstateDTO;
import com.robot.homeobot.exception.MyException;
import com.robot.homeobot.model.RealEstate;
import com.robot.homeobot.model.User;
import com.robot.homeobot.repository.RealEstateRepository;
import com.robot.homeobot.services.realestate.RealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/api/real-estate", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class RealEstateController {

    @Autowired
    private RealEstateService realEstateService;

    @Autowired
    private RealEstateRepository realEstateRepository;

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

    @PostMapping("/update-owners")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RealEstate>> updateRealEstateOwnersForAdmin(@RequestBody List<ChangeRealEstateOwnersDTO> dtos) {
        List<RealEstate> allRealEstate = null;
        try {
            allRealEstate = realEstateService.updateRealEstateOwnersForAdmin(dtos);
        } catch (MyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return new ResponseEntity<>(allRealEstate, HttpStatus.OK);
    }

    @GetMapping("/owned-real-estate")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<RealEstate>> getOwnerRealEstate() {
        List<RealEstate> allOwnerRealEstate = realEstateService.getAllOwnerRealEstate();

        return new ResponseEntity<>(allOwnerRealEstate, HttpStatus.OK);
    }

    @PostMapping("/update-renters")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<RealEstate>> updateRealEstateRentersForOwner(@RequestBody List<ChangeRealEstateRentersDTO> dtos) {
        List<RealEstate> allOwnerRealEstate = null;
        try {
            allOwnerRealEstate = realEstateService.updateRealEstateRentersForOwner(dtos);
        } catch (MyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return new ResponseEntity<>(allOwnerRealEstate, HttpStatus.OK);
    }
}
