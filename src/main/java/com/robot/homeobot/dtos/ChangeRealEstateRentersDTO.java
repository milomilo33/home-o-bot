package com.robot.homeobot.dtos;

import java.util.List;

public class ChangeRealEstateRentersDTO {
    private Long realEstateId;
    private List<String> renterUsernames;

    public ChangeRealEstateRentersDTO() {}

    public ChangeRealEstateRentersDTO(Long realEstateId, List<String> renterUsernames) {
        this.realEstateId = realEstateId;
        this.renterUsernames = renterUsernames;
    }

    public Long getRealEstateId() {
        return realEstateId;
    }

    public void setRealEstateId(Long realEstateId) {
        this.realEstateId = realEstateId;
    }

    public List<String> getRenterUsernames() {
        return renterUsernames;
    }

    public void setRenterUsernames(List<String> renterUsernames) {
        this.renterUsernames = renterUsernames;
    }
}
