package com.robot.homeobot.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "revocation")
public class Revocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "revocation_id",
            nullable = false,
            unique = true
    )
    private Long id;
    private String CN;
    private String reason;
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean permanent;
    private LocalDateTime revocationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCN() {
        return CN;
    }

    public void setCN(String CN) {
        this.CN = CN;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public Revocation(String CN, String reason, boolean permanent, LocalDateTime revocationDate) {
        this.CN = CN;
        this.reason = reason;
        this.permanent = permanent;
        this.revocationDate = revocationDate;
    }

    public Revocation() {}

    public LocalDateTime getRevocationDate() {
        return revocationDate;
    }

    public void setRevocationDate(LocalDateTime revocationDate) {
        this.revocationDate = revocationDate;
    }
}
