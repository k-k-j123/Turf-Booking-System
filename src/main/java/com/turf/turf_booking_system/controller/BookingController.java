package com.turf.turf_booking_system.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turf.turf_booking_system.model.bookings;



@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private com.turf.turf_booking_system.service.bookingService bookingService;

    @PreAuthorize("hasRole('customer') or hasRole('admin') or hasRole('super_admin')")
    @PostMapping
    public ResponseEntity<bookings> createBooking(@RequestBody bookings booking) {
        bookings newBooking = bookingService.createBooking(booking);
        return ResponseEntity.ok(newBooking);
    }

    @PreAuthorize("hasRole('admin') or hasRole('super_admin')")
    @GetMapping("/turf/{turfId}")
    public ResponseEntity<List<bookings>> getBookingsByTurf(@PathVariable Long turfId) {
        return ResponseEntity.ok(bookingService.getBookingsByTurf(turfId));
    }

    @PreAuthorize("hasRole('admin') or hasRole('super_admin')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<bookings>> getBookingsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
    }

    @PreAuthorize("hasRole('customer') or hasRole('admin') or hasRole('super_admin')")
    @GetMapping("/date")
    public ResponseEntity<List<bookings>> getBookingsByDate(@RequestParam LocalDate date) {
        return ResponseEntity.ok(bookingService.getBookingsByDate(date));
    }

    @PreAuthorize("hasRole('admin') or hasRole('super_admin')")
    @PutMapping("/{bookingId}/{status}")
    public ResponseEntity<bookings> updateBookingStatus(@PathVariable Long bookingId, @PathVariable String status) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(bookingId, status));    
    }
}

