package com.turf.turf_booking_system.service.iml;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turf.turf_booking_system.model.bookings;
import com.turf.turf_booking_system.repository.BookingRepository;
import com.turf.turf_booking_system.service.bookingService;

import jakarta.transaction.Transactional;

@Service
public class bookingServiceImplement implements bookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public bookings createBooking(bookings booking) {
        // Add business logic for booking creation
        return bookingRepository.save(booking);
    }

    @Override
    public List<bookings> getBookingsByTurf(Long turfId) {
        return bookingRepository.findByTurfId(turfId);
    }

    @Override
    public List<bookings> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<bookings> getBookingsByDate(LocalDate date) {
        return bookingRepository.findByBookingDate(date);
    }

    @Override
    @Transactional
    public bookings updateBookingStatus(Long bookingId, String status) {
        bookingRepository.updateBookingStatus(bookingId, status);
        return bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
    }
}