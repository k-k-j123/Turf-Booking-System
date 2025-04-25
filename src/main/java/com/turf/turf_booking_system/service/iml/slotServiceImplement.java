package com.turf.turf_booking_system.service.iml;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turf.turf_booking_system.model.Slot;
import com.turf.turf_booking_system.repository.SlotRepository;
import com.turf.turf_booking_system.service.slotService;

@Service
public class slotServiceImplement implements slotService{

    @Autowired
    private SlotRepository slotRepository;

    @Override
    public Slot createSlot(Slot slot) {
        return slotRepository.save(slot);
    }

    @Override
    public List<Slot> getSlotsByTurfAndDate(Long turf_id, LocalDate date) {
        return slotRepository.findByTurfIdAndDate(turf_id, date);
    }

    @Override
    public Slot updateSlot(Long slot_id, Slot updatedSlot) {
        Slot existingSlot = slotRepository.findById(slot_id).orElseThrow(() -> new RuntimeException("Slot not found"));
        existingSlot.setStart_time(updatedSlot.getStart_time());
        existingSlot.setEnd_time(updatedSlot.getEnd_time());
        existingSlot.setIsAvailable(updatedSlot.getIsAvailable());
        return slotRepository.save(existingSlot);
    }

    @Override
    public String deleteSlot(Long slot_id) {
        slotRepository.deleteById(slot_id);
        return "Slot Deleted Succesfully";
    }

}
