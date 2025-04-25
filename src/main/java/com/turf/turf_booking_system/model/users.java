package com.turf.turf_booking_system.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class users implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long user_Id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;
    private boolean isApproved;
    
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<turfs> turfs;

    public List<turfs> getTurfs() {
        return turfs;
    }

    public void setTurfs(List<turfs> turfs) {
        this.turfs = turfs;
    }

    @JsonIgnore // Prevent serialization of authorities
    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    //Constructor
    public users(Long id,String name,String email,String phone,String password,String role,boolean apprStatus){
        this.user_Id=id;
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.password=password;
        this.role=role;
        this.isApproved=apprStatus;
    }

    //Empty Constructor
    public users(){}

    // Getters and Setters

    public Long getUserId() {
        return user_Id;
    }

    public void setUserId(Long userId) {
        this.user_Id = userId;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String username) {
        this.name = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
        
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPsw() {
        return password;
    }

    public void setPsw(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Return authorities based on the role stored in the user entity
        return Collections.singletonList(() -> "ROLE_" + role);
    }

    @Override
    public String getPassword() {
        return password;
    }
}

