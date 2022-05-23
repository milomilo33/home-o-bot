package com.robot.homeobot.controllers;

import com.robot.homeobot.dtos.CSRDTO;
import com.robot.homeobot.services.pki.CSRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.util.*;
import java.util.List;

@RestController
@RequestMapping("/api/csr")
public class CSRController {

    @Autowired
    private CSRService csrService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CSRDTO>> getAllCSRs() {
        List<CSRDTO> CSRs;
        try {
            CSRs = csrService.getAllCSRs();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

        return new ResponseEntity<>(CSRs, HttpStatus.OK);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    //    @PreAuthorize("hasAnyRole('')")
    public ResponseEntity<?> createCSR(@RequestBody CSRDTO csrDTO) {
        try {
            csrService.generateCSR(csrDTO.getCN(), csrDTO.getOU(), csrDTO.getO(), csrDTO.getL(), csrDTO.getST(), csrDTO.getC());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/reject/{CN}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectCSR(@PathVariable String CN) {
        try {
            csrService.deleteCSRAndPrivateKey(CN);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
