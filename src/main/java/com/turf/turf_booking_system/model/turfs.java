package com.turf.turf_booking_system.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "turfs")
public class turfs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Auto Increment Turf ID.
    private Long turf_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private BigDecimal pricePerHour;

    @Column
    private BigDecimal ground_width;

    @Column
    private BigDecimal ground_length;

    @Column
    private BigDecimal ground_height;

    @Column(length = 2048)
    private String image;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JsonIgnore // Prevent recursion
    @JoinColumn(name = "manager_id", nullable = false)  // Foreign key column
    private users manager;  // The manager of this turf (mapped to user)

    public users getManager() {
        return manager;
    }

    public void setManager(users manager) {
        this.manager = manager;
    }

    //getters and Setter
    public Long getTurf_id() {
        return turf_id;
    }

    public void setTurf_id(Long turf_id) {
        this.turf_id = turf_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(BigDecimal pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public BigDecimal getGround_width() {
        return ground_width;
    }

    public void setGround_width(BigDecimal ground_width) {
        this.ground_width = ground_width;
    }

    public BigDecimal getGround_length() {
        return ground_length;
    }

    public void setGround_length(BigDecimal ground_length) {
        this.ground_length = ground_length;
    }

    public BigDecimal getGround_height() {
        return ground_height;
    }

    public void setGround_height(BigDecimal ground_height) {
        this.ground_height = ground_height;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

