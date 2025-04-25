package com.turf.turf_booking_system.service;

import java.util.List;

import com.turf.turf_booking_system.model.users;

public interface userService {
    public String createUser(users user);
    public String updateUser(users user);
    public String delete(Long userId);
    public users getUser(Long userId);
    public List<users> getUsers(String name);
    public List<users> getAllUser();
    public users findByEmail(String email);
    public List<users> getPendingAdmins();
    public Boolean getIsApproved(Long userId);
    public void approveAdmin(Long userId);
}
