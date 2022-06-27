package com.robot.homeobot.services.realestate;

import com.robot.homeobot.dto.UserRequest;
import com.robot.homeobot.dtos.ChangeRealEstateOwnersDTO;
import com.robot.homeobot.dtos.ChangeRealEstateRentersDTO;
import com.robot.homeobot.dtos.DeviceConfigDTO;
import com.robot.homeobot.dtos.RealEstateDTO;
import com.robot.homeobot.exception.MyException;
import com.robot.homeobot.model.Device;
import com.robot.homeobot.model.RealEstate;
import com.robot.homeobot.model.User;
import com.robot.homeobot.repository.RealEstateRepository;
import com.robot.homeobot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RealEstateServiceImpl implements RealEstateService {

    @Autowired
    private RealEstateRepository realEstateRepository;

    @Autowired
    private UserRepository userRepository;

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

    @Override
    @Transactional
    public List<RealEstate> updateRealEstateOwnersForAdmin(List<ChangeRealEstateOwnersDTO> dtos) throws MyException {
        for (ChangeRealEstateOwnersDTO dto : dtos) {
            RealEstate realEstate = this.findById(dto.getRealEstateId());
            User owner = userRepository.findByUsername(dto.getOwnerUsername());
            if (!owner.getRoles().get(0).getName().equals("ROLE_OWNER")) {
                throw new MyException("User is not an owner");
            }
            realEstate.setOwner(owner);
        }

        return realEstateRepository.findAll();
    }

    @Override
    @Transactional
    public List<RealEstate> getAllOwnerRealEstate() {
        User currentOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Set<RealEstate> allOwnerRealEstate = currentOwner.getOwnedRealEstate();
        if (allOwnerRealEstate == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(allOwnerRealEstate);
    }

    @Override
    @Transactional
    public List<RealEstate> updateRealEstateRentersForOwner(List<ChangeRealEstateRentersDTO> dtos) throws MyException {
        User currentOwner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        for (ChangeRealEstateRentersDTO dto : dtos) {
            RealEstate realEstate = this.findById(dto.getRealEstateId());

            if (!realEstate.getOwner().getUsername().equals(currentOwner.getUsername())) {
                throw new MyException("Real estate does not belong to current user");
            }

            // add real estate to rented collection if not already rented
            for (String renterUsername : dto.getRenterUsernames()) {
                User renter = userRepository.findByUsername(renterUsername);
                if (!renter.getRoles().get(0).getName().equals("ROLE_RENTER")) {
                    throw new MyException("User is not a renter");
                }
                Set<RealEstate> rentedRealEstate = renter.getRentedRealEstate();
                if (renter.getRentedRealEstate() == null) {
                    rentedRealEstate = new HashSet<>();
                }
                boolean alreadyRenting = rentedRealEstate
                                                        .stream()
                                                        .anyMatch(re -> re.getId().equals(realEstate.getId()));
                if (!alreadyRenting) {
                    rentedRealEstate.add(realEstate);
                }
            }

            // remove real estate from renters not specified in request
            for (User user : userRepository.findAll()) {
                boolean userSpecifiedInRequest = dto.getRenterUsernames().contains(user.getUsername());
                if (user.getRoles().get(0).getName().equals("ROLE_RENTER") && !userSpecifiedInRequest) {
                    Set<RealEstate> rentedRealEstate = user.getRentedRealEstate();
                    if (rentedRealEstate != null && !rentedRealEstate.isEmpty()) {
                        boolean realEstateRentedToUser = rentedRealEstate
                                .stream()
                                .anyMatch(re -> re.getId().equals(realEstate.getId()));
                        if (realEstateRentedToUser) {
                            rentedRealEstate = rentedRealEstate
                                    .stream()
                                    .filter(re -> !re.getId().equals(realEstate.getId()))
                                    .collect(Collectors.toSet());
                            user.setRentedRealEstate(rentedRealEstate);
                        }
                    }
                }
            }
        }

        return new ArrayList<>(currentOwner.getOwnedRealEstate());
    }
}
