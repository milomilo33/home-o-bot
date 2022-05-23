package com.robot.homeobot.controllers;

import com.robot.homeobot.dtos.AllCertificatesDTO;
import com.robot.homeobot.dtos.CSRDTO;
import com.robot.homeobot.dtos.NewCertificateDTO;
import com.robot.homeobot.services.pki.CSRService;
import com.robot.homeobot.services.pki.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<AllCertificatesDTO>> getAllCertificates() {
        List<AllCertificatesDTO> certificates;
        try {
            certificates = certificateService.getAllCertificates();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    //    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> createCertificate(@RequestBody NewCertificateDTO newCertificateDTO) {
        try {
            certificateService.createCertificate(newCertificateDTO, newCertificateDTO.getCN());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/revoke")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> revokeCertificate(@RequestParam("CN") String CN, @RequestParam("reason") String reason) {
        try {
            certificateService.revokeCertificate(CN, reason);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/unrevoke")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> unrevokeCertificate(@RequestParam("CN") String CN) {
        try {
            certificateService.unrevokeCertificateOnHold(CN);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
