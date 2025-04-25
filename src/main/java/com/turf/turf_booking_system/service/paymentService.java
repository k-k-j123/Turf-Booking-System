package com.turf.turf_booking_system.service;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.turf.turf_booking_system.model.Payment;
import com.turf.turf_booking_system.repository.PaymentRepository;

@Service
public class paymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    public String createOrder(double amount) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);

        JSONObject options = new JSONObject();
        options.put("amount", (int) (amount * 100)); // Convert to paise
        options.put("currency", "INR");
        options.put("receipt", "txn_" + System.currentTimeMillis());

        Order order = razorpay.orders.create(options);
        return order.toString();
    }

    // Save Payment
    public Payment savePayment(Long booking_id, BigDecimal amount, String payment_method) {
        Payment payment = new Payment(booking_id, amount, payment_method);
        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }
}
