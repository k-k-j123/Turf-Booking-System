package com.turf.turf_booking_system.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.turf.turf_booking_system.model.turfs;
import com.turf.turf_booking_system.model.users;
import com.turf.turf_booking_system.service.turfService;

@RestController
@RequestMapping("/api/turfs")
public class TurfController {

    UserController userController;
    @Autowired
    turfService turfservice;
    public TurfController(turfService turfservice)
    {
        this.turfservice=turfservice;
    }
    
    @PreAuthorize("hasRole('admin') or hasRole('super_admin')")
    @PostMapping
    public ResponseEntity<?> addTurf(@RequestParam("name") String name,
        @RequestParam("location") String location,
        @RequestParam("pricePerHour") BigDecimal pricePerHour,
        @RequestParam("ground_width") BigDecimal groundWidth,
        @RequestParam("ground_length") BigDecimal groundLength,
        @RequestParam("ground_height") BigDecimal groundHeight,
        @RequestParam("image") String image,
        @RequestParam("manager") String managerJson
    ) {
        try {
            // Deserialize manager JSON string to Manager object
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            users manager = objectMapper.readValue(managerJson, users.class);

            // Create a Turf object
            turfs turf = new turfs();
            turf.setName(name);
            turf.setLocation(location);
            turf.setPricePerHour(pricePerHour);
            turf.setGround_width(groundWidth);
            turf.setGround_length(groundLength);
            turf.setGround_height(groundHeight);
            turf.setImage(image);
            turf.setManager(manager);

            // Save turf to database (pseudo-code, replace with actual repository/service call)
            turfs savedTurf = turfservice.addTurf(turf);

            return ResponseEntity.ok(savedTurf); // Return saved Turf as response
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create turf");
        }
    }

    @PreAuthorize("hasRole('admin') or hasRole('super_admin')")
    @PutMapping
    public String updateTurf(@RequestBody turfs turf) {
        turfservice.updateTurf(turf);
        return "Sucessfully Updated the Turf "+turf.getName();
    }

    @PreAuthorize("hasRole('admin') or hasRole('super_admin')")
    @DeleteMapping("/{turfId}")
    public ResponseEntity<String> deleteTurf(@PathVariable Long turfId) {
        turfservice.deleteTurf(turfId);
        return ResponseEntity.ok("Turf deleted successfully");
    }

    @PreAuthorize("hasRole('customer') or hasRole('admin') or hasRole('super_admin')")
    @GetMapping
    public ResponseEntity<List<turfs>> listAllTurfs() {
        return ResponseEntity.ok(turfservice.listAllTurfs());
    }

    @PreAuthorize("hasRole('customer') or hasRole('admin') or hasRole('super_admin')")
    @GetMapping("/search")
    public ResponseEntity<List<turfs>> searchTurfs(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location) {
        return ResponseEntity.ok(turfservice.searchTurfs(name, location));
    }

    @PreAuthorize("hasRole('customer') or hasRole('admin') or hasRole('super_admin')")
    @GetMapping("/{turfId}")
    public ResponseEntity<turfs> getTurfById(@PathVariable Long turfId) {
        return ResponseEntity.ok(turfservice.getTurfById(turfId));
    }

}
