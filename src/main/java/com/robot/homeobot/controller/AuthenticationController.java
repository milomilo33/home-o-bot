package com.robot.homeobot.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.robot.homeobot.dto.JwtAuthenticationRequest;
import com.robot.homeobot.dto.UserRequest;
import com.robot.homeobot.dto.UserTokenState;
import com.robot.homeobot.exception.ResourceConflictException;
import com.robot.homeobot.model.JwtBlacklist;
import com.robot.homeobot.model.User;
import com.robot.homeobot.repository.JwtBlacklistRepository;
import com.robot.homeobot.services.user.UserService;
import com.robot.homeobot.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Map;


//Kontroler zaduzen za autentifikaciju korisnika
@RestController
@Validated
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtBlacklistRepository jwtBlacklistRepository;

    // Prvi endpoint koji pogadja korisnik kada se loguje.
    // Tada zna samo svoje korisnicko ime i lozinku i to prosledjuje na backend.
    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {

        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        User user = (User) authentication.getPrincipal();
        String fingerprint = tokenUtils.generateFingerprint();
        String jwt = tokenUtils.generateToken(user.getUsername(), fingerprint);
        int expiresIn = tokenUtils.getExpiredIn();

        // Kreiraj cookie
        // String cookie = "__Secure-Fgp=" + fingerprint + "; SameSite=Strict; HttpOnly; Path=/; Secure";  // kasnije mozete probati da postavite i ostale atribute, ali tek nakon sto podesite https
        String cookie = "Fingerprint=" + fingerprint + "; HttpOnly; Path=/";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", cookie);

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok().headers(headers).body(new UserTokenState(jwt, expiresIn));
    }

    // Endpoint za registraciju novog korisnika
    @PostMapping("/signup")
    @PreAuthorize("hasAuthority('CREATE_USER')")
    public ResponseEntity<User> addUser(@RequestBody UserRequest userRequest, UriComponentsBuilder ucBuilder) {

        if(!userRequest.getPassword().matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?\\d)(?=.*?[#?!@$ %^&*-]).{8,}$")){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"password must be minimum 8 characters, contain one number ,contain at least one upper case letter, and one special character");
        }

        if(!userRequest.getEmail().matches("/^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$/")){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid email format");
        }
        User existUser = this.userService.findByUsername(userRequest.getUsername());

        if (existUser != null) {
            throw new ResourceConflictException(userRequest.getId(), "Username already exists");
        }

        User user = this.userService.save(userRequest);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping(value = "/destroy", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JwtBlacklist logout(@RequestBody Map<String,String> json)  {

        String token = json.get("token");

        JwtBlacklist jwtBlacklist = new JwtBlacklist();
        jwtBlacklist.setToken(token);
        jwtBlacklistRepository.save(jwtBlacklist);

        return jwtBlacklistRepository.save(jwtBlacklist);
    }
}