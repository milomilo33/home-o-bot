package com.robot.homeobot.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.robot.homeobot.dto.UserRequest;
import com.robot.homeobot.model.Device;
import com.robot.homeobot.model.Role;
import com.robot.homeobot.model.User;
import com.robot.homeobot.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


// Primer kontrolera cijim metodama mogu pristupiti samo autorizovani korisnici
@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;


    // Za pristup ovoj metodi neophodno je da ulogovani korisnik ima READ_USER permisiju
    // Ukoliko nema, server ce vratiti gresku 403 Forbidden
    // Korisnik jeste autentifikovan, ali nije autorizovan da pristupi resursu
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('READ_USER')")
    public User loadById(@PathVariable Long userId) {
        return this.userService.findById(userId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('READ_USERS')")
    public List<User> loadAll() {
        return this.userService.findAll();
    }

    @GetMapping("/whoami")
    @PreAuthorize("hasAuthority('FIND_USER')")
    public User user(Principal user) {
        return this.userService.findByUsername(user.getName());
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public User user(@RequestBody UserRequest userRequest, @PathVariable Long userId){
        return this.userService.updateUser(userId, userRequest);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            this.userService.deleteUser(userId);
            return ResponseEntity.ok().body("User deleted");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user-roles")
    public List<Role> getAllRoles() {
        return this.userService.getAllRoles();
    }

    @GetMapping("/all-owners")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllOwners() {
        List<User> allOwners = userService.findAll()
                .stream().filter(u -> u.getRoles().get(0).getName().equals("ROLE_OWNER")).collect(Collectors.toList());

        return new ResponseEntity<>(allOwners, HttpStatus.OK);
    }

    @GetMapping("/all-renters")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<User>> getAllRenters() {
        List<User> allRenters = userService.findAll()
                .stream().filter(u -> u.getRoles().get(0).getName().equals("ROLE_RENTER")).collect(Collectors.toList());

        return new ResponseEntity<>(allRenters, HttpStatus.OK);
    }

    @GetMapping("/all-devices-for-role")
    @PreAuthorize("hasAnyRole('OWNER', 'RENTER')")
    public ResponseEntity<List<Device>> getAllDevicesForOwnerOrRenter() {
        List<Device> devices = this.userService.getAllDevicesForOwnerOrRenter();

        return new ResponseEntity<>(devices, HttpStatus.OK);
    }
}
