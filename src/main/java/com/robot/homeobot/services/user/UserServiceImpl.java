package com.robot.homeobot.services.user;

import java.util.ArrayList;
import java.util.List;

import com.robot.homeobot.dto.UserRequest;
import com.robot.homeobot.dtos.DeviceDTO;
import com.robot.homeobot.model.Device;
import com.robot.homeobot.model.RealEstate;
import com.robot.homeobot.model.Role;
import com.robot.homeobot.model.User;
import com.robot.homeobot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public User findById(Long id) throws AccessDeniedException {
        return userRepository.findById(id).orElseGet(null);
    }

    public List<User> findAll() throws AccessDeniedException {
        return userRepository.findAll();
    }

    @Override
    public User save(UserRequest userRequest) {
        User u = new User();
        u.setUsername(userRequest.getUsername());



        // pre nego sto postavimo lozinku u atribut hesiramo je kako bi se u bazi nalazila hesirana lozinka
        // treba voditi racuna da se koristi isi password encoder bean koji je postavljen u AUthenticationManager-u kako bi koristili isti algoritam
        u.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        u.setFirstName(userRequest.getFirstname());
        u.setLastName(userRequest.getLastname());
        u.setEnabled(true);
        u.setEmail(userRequest.getEmail());

        // u primeru se registruju samo obicni korisnici i u skladu sa tim im se i dodeljuje samo rola USER
        List<Role> roles = new ArrayList<>();
        for(String role : userRequest.getRoles()){
            roles.addAll(roleService.findByName(role));
        }
        if (roles.size() == 0) {
            roles.addAll(roleService.findByName("ROLE_USER"));
        }
        u.setRoles(roles);

        return this.userRepository.save(u);
    }

    @Override
    public User updateUser(Long userId, UserRequest userRequest) {
        User u  = this.userRepository.findById(userId).orElseThrow();
        if (userRequest.getRoles().size() > 0) {
            List<Role> roles = new ArrayList<>();
            for (String role : userRequest.getRoles()) {
                roles.addAll(roleService.findByName(role));
            }
            u.setRoles(roles);
        }
        return u;
    }

    @Override
    public void deleteUser(Long userId) {
        User u = this.userRepository.findById(userId).orElseThrow();
        userRepository.delete(u);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @Override
    @Transactional
    public List<DeviceDTO> getAllDevicesForOwnerOrRenter() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<DeviceDTO> devices = new ArrayList<>();
        if (currentUser.getRoles().get(0).getName().equals("ROLE_OWNER")) {
            for (RealEstate re : currentUser.getOwnedRealEstate()) {
                for (Device device : re.getDevices()) {
                    devices.add(new DeviceDTO(device));
                }
            }
        }
        else if (currentUser.getRoles().get(0).getName().equals("ROLE_RENTER")) {
            for (RealEstate re : currentUser.getRentedRealEstate()) {
                for (Device device : re.getDevices()) {
                    devices.add(new DeviceDTO(device));
                }
            }
        }

        return devices;
    }

}
