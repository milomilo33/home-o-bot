package com.robot.homeobot.model;

import java.sql.Timestamp;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

// POJO koji implementira Spring Security UserDetails interfejs koji specificira
// osnovne osobine Spring korisnika (koje role ima, da li je nalog zakljucan, istekao, da li su kredencijali istekli)
@Entity
@Table(name="USERS")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "enabled")
    private boolean enabled;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private Set<RealEstate> ownedRealEstate;

//    @ManyToMany(mappedBy = "renters", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "renters_real_estate",
            inverseJoinColumns = @JoinColumn(name = "real_estate_id",
                    referencedColumnName = "id"),
            joinColumns = @JoinColumn(name = "renter_id", referencedColumnName = "id"))
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private Set<RealEstate> rentedRealEstate;

    public Set<RealEstate> getOwnedRealEstate() {
        return ownedRealEstate;
    }

    public void setOwnedRealEstate(Set<RealEstate> ownedRealEstate) {
        this.ownedRealEstate = ownedRealEstate;
    }

    public Set<RealEstate> getRentedRealEstate() {
        return rentedRealEstate;
    }

    public void setRentedRealEstate(Set<RealEstate> rentedRealEstate) {
        this.rentedRealEstate = rentedRealEstate;
    }

    @Column(name = "last_password_reset_date")
    private Timestamp lastPasswordResetDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> permissions = new ArrayList<>(20);
        for (Role role : this.roles) {
            permissions.addAll(role.getPrivileges());
        }
        permissions.addAll(this.roles);
        return permissions;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        Timestamp now = new Timestamp(new Date().getTime());
        this.setLastPasswordResetDate(now);
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Role> getRoles() {
        return this.roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Timestamp getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}