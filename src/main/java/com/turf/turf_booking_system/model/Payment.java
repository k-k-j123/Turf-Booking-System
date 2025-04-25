package com.turf.turf_booking_system.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payment_id;

    @Column(nullable = false)
    private Long booking_id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String payment_method;

    @Column(nullable = false, updatable = false)
    private LocalDateTime payment_date;

    // Default Constructor
    public Payment() {
    }

    // Constructor
    public Payment(Long booking_id, BigDecimal amount, String payment_method) {
        this.booking_id = booking_id;
        this.amount = amount;
        this.payment_method = payment_method;
        this.payment_date = LocalDateTime.now();
    }

    public Long getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(Long payment_id) {
        this.payment_id = payment_id;
    }

    public Long getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(Long booking_id) {
        this.booking_id = booking_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public LocalDateTime getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(LocalDateTime payment_date) {
        this.payment_date = payment_date;
    }

    @Override
    public String toString() {
        return "Payment { payment_id=" + payment_id + ", booking_id=" + booking_id + ", amount=" + amount + 
               ", payment_method='" + payment_method + "', payment_date=" + payment_date + " }";
    }
}
