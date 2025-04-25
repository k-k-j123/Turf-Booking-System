package com.turf.turf_booking_system.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turf.turf_booking_system.model.users;
import com.turf.turf_booking_system.service.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class RegistrationController {

    @Autowired
    private userService userservice;

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<String> createUser(@RequestBody users user){
        userservice.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully.");
    }
}