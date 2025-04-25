package com.turf.turf_booking_system.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.turf.turf_booking_system.model.Slot;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
    @Query("SELECT ts FROM Slot ts WHERE ts.turf.turf_id = :turf_id AND ts.date = :date")
    List<Slot> findByTurfIdAndDate(Long turf_id, LocalDate date);
    @Query("SELECT ts FROM Slot ts WHERE ts.turf.turf_id = :turf_id")
    List<Slot> findByTurfId(Long turf_id);
    @Query("SELECT ts FROM Slot ts WHERE ts.date = :date")
    List<Slot> findByDate(LocalDate date);
}
