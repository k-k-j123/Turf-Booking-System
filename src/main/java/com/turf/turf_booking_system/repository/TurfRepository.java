package com.turf.turf_booking_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turf.turf_booking_system.model.turfs;

@Repository
public interface TurfRepository extends JpaRepository<turfs, Long>{
    List<turfs> findByNameContainingIgnoreCase(String name);
    List<turfs> findByLocationContainingIgnoreCase(String location);
}
