package com.turf.turf_booking_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.razorpay.RazorpayException;
import com.turf.turf_booking_system.model.Payment;
import com.turf.turf_booking_system.service.paymentService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private paymentService paymentService;

    // Create Payment
    @PostMapping("/createOrder")
    public ResponseEntity<String> createRazorpayOrder(@RequestParam double amount) {
        try {
            String order = paymentService.createOrder(amount);
            return ResponseEntity.ok(order);
        } catch (RazorpayException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating order: " + e.getMessage());
        }
    }

    // Save Payment After Razorpay Transaction Success
    // Save Payment
    @PostMapping("/save")
    public ResponseEntity<Payment> savePayment(@RequestBody Payment payment) {
        System.out.println("Payment: "+payment.getPayment_method());
        Payment savedPayment = paymentService.savePayment(
            payment.getBooking_id(), 
            payment.getAmount(), 
            payment.getPayment_method()
        );
        return ResponseEntity.ok(savedPayment);
    }

    // Get Payments by Booking ID
    @GetMapping("/{bookingId}")
    public List<Payment> getPaymentsByBooking(@PathVariable Long bookingId) {
        return paymentService.getPaymentsByBookingId(bookingId);
    }
}
