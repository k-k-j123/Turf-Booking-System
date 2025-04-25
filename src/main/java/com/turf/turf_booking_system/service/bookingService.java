package com.turf.turf_booking_system.service;

import java.time.LocalDate;
import java.util.List;

import com.turf.turf_booking_system.model.bookings;

public interface bookingService {
    bookings createBooking(bookings booking);

    List<bookings> getBookingsByTurf(Long turfId);

    List<bookings> getBookingsByUser(Long userId);

    List<bookings> getBookingsByDate(LocalDate date);

    bookings updateBookingStatus(Long bookingId, String status);
}
