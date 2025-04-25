package com.turf.turf_booking_system.service;

import java.time.LocalDate;
import java.util.List;

import com.turf.turf_booking_system.model.Slot;

public interface slotService {
    public Slot createSlot(Slot slot);
    public List<Slot> getSlotsByTurfAndDate(Long turf_id, LocalDate date);
    public Slot updateSlot(Long slot_id, Slot updatedSlot);
    public String deleteSlot(Long slot_id);
}
