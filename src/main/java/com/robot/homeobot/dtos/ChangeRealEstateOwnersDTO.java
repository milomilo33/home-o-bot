package com.robot.homeobot.dtos;

public class ChangeRealEstateOwnersDTO {
    private Long realEstateId;
    private String ownerUsername;

    public ChangeRealEstateOwnersDTO() {}

    public ChangeRealEstateOwnersDTO(Long realEstateId, String ownerUsername) {
        this.realEstateId = realEstateId;
        this.ownerUsername = ownerUsername;
    }

    public Long getRealEstateId() {
        return realEstateId;
    }

    public void setRealEstateId(Long realEstateId) {
        this.realEstateId = realEstateId;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }
}
