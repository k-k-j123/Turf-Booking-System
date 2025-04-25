package com.turf.turf_booking_system.service.iml;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turf.turf_booking_system.model.turfs;
import com.turf.turf_booking_system.repository.TurfRepository;
import com.turf.turf_booking_system.service.turfService;

@Service
public class turfServiceImplement implements turfService{

    @Autowired
    TurfRepository turfRepository;
    public turfServiceImplement(TurfRepository turfrepository){
        this.turfRepository=turfrepository;
    }

    @Override
    public turfs addTurf(turfs turf) {
        return turfRepository.save(turf);
    }

    @Override
    public String updateTurf(turfs turf) {
        turfRepository.save(turf);
        return "SuceessFully Updated the Turf "+turf.getName();
    }

    @Override
    public String deleteTurf(Long turfId) {
        turfRepository.deleteById(turfId);
        return "SuceesFully Delated Turf with ID:"+turfId;
    }

    @Override
    public List<turfs> listAllTurfs() {
        return turfRepository.findAll();
    }

    @Override
    public List<turfs> searchTurfs(String name, String location) {
        if (name != null) {
            return turfRepository.findByNameContainingIgnoreCase(name);
        } else if (location != null) {
            return turfRepository.findByLocationContainingIgnoreCase(location);
        } else {
            return turfRepository.findAll();
        }
    }

    @Override
    public turfs getTurfById(Long turfId) {
        Optional<turfs> turf = turfRepository.findById(turfId);
        return turf.orElse(null);
    }

    

}
