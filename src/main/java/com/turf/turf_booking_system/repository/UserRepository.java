package com.turf.turf_booking_system.repository;

import com.turf.turf_booking_system.model.users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<users, Long> {
    //find users by name.
    @Query("SELECT u FROM users u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<users> findByName(String name);
    Optional<users> findByEmail(String email);
    List<users> findByRoleAndIsApproved(String role, Boolean isApproved);
}
