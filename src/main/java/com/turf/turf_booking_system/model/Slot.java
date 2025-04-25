package com.turf.turf_booking_system.model;

import java.time.LocalDate;
import java.time.LocalTime;

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
@Table(name = "timeslots")
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slot_id;

    @ManyToOne
    @JoinColumn(name = "turf_id", nullable = false)
    private turfs turf;

    @Column
    private LocalTime start_time;

    @Column
    private LocalTime end_time;

    @Column
    private LocalDate date;

    @Column
    private Boolean is_available = true;

    public Long getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(Long slot_id) {
        this.slot_id = slot_id;
    }

    public turfs getTurf() {
        return turf;
    }

    public void setTurf(turfs turf) {
        this.turf = turf;
    }

    public LocalTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalTime start_time) {
        this.start_time = start_time;
    }

    public LocalTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalTime end_time) {
        this.end_time = end_time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getIsAvailable() {
        return is_available;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.is_available = isAvailable;
    }
}
