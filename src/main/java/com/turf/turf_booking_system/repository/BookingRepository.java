package com.turf.turf_booking_system.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.turf.turf_booking_system.model.bookings;

@Repository
public interface BookingRepository extends JpaRepository<bookings, Long> {

    // Fetch all bookings for a specific turf
    @Query("SELECT b FROM bookings b WHERE b.turf.turf_id = :turfId")
    List<bookings> findByTurfId(@Param("turfId") Long turfId);

    // Fetch all bookings for a specific user
    @Query("SELECT b FROM bookings b WHERE b.user.user_Id = :userId")
    List<bookings> findByUserId(@Param("userId") Long userId);

    // Fetch all bookings for a specific date
    @Query("SELECT b FROM bookings b WHERE b.booking_date = :date")
    List<bookings> findByBookingDate(@Param("date") LocalDate date);

    // Update the status of a booking
    @Modifying
    @Query("UPDATE bookings b SET b.status = :status WHERE b.booking_id = :bookingId")
    void updateBookingStatus(@Param("bookingId") Long bookingId, @Param("status") String status);
}
