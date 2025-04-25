package com.turf.turf_booking_system.service;

import java.util.List;

import com.turf.turf_booking_system.model.turfs;

public interface turfService {
    public turfs addTurf(turfs turf);
    public String updateTurf(turfs turf);
    public String deleteTurf(Long turfId);
    public List<turfs> listAllTurfs();
    public List<turfs> searchTurfs(String name, String location);
    public turfs getTurfById(Long turfId);
}