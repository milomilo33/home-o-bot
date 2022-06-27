package com.robot.homeobot.services.realestate;

import com.robot.homeobot.dto.UserRequest;
import com.robot.homeobot.dtos.ChangeRealEstateOwnersDTO;
import com.robot.homeobot.dtos.ChangeRealEstateRentersDTO;
import com.robot.homeobot.dtos.DeviceConfigDTO;
import com.robot.homeobot.dtos.RealEstateDTO;
import com.robot.homeobot.exception.MyException;
import com.robot.homeobot.model.RealEstate;
import com.robot.homeobot.model.User;

import java.util.List;

public interface RealEstateService {
    RealEstate findById(Long id);
    List<RealEstate> findAll();
    RealEstate save(RealEstateDTO realEstateDTO);
    List<RealEstate> updateRealEstateOwnersForAdmin(List<ChangeRealEstateOwnersDTO> dtos) throws MyException;
    List<RealEstate> getAllOwnerRealEstate();
    List<RealEstate> updateRealEstateRentersForOwner(List<ChangeRealEstateRentersDTO> dtos) throws MyException;
}
