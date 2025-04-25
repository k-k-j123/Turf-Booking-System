package com.turf.turf_booking_system.service.iml;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.turf.turf_booking_system.model.users;
import com.turf.turf_booking_system.repository.UserRepository;
import com.turf.turf_booking_system.service.userService;

@Service
public class userServiceImplement implements userService, UserDetailsService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public userServiceImplement(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public String createUser(users user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setPsw(passwordEncoder.encode(user.getPsw()));
        userRepository.save(user);
        return "User Created Successfully";
    }

    @Override
    public String updateUser(users user) {
        // Fetch the existing user
        users existingUser = userRepository.findById(user.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Only encode the password if it has been changed
        if (!user.getPsw().equals(existingUser.getPsw())) {
            user.setPsw(passwordEncoder.encode(user.getPsw()));
        }

        // Update other fields (if applicable)
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());

        // Save the updated user
        userRepository.saveAndFlush(existingUser);

        return "User Successfully Updated";
    }

    @Override
    public String delete(Long userId) {
        userRepository.deleteById(userId);
        return "User with "+userId+" Deleted Sucessfully";
    }

    @Override
    public users getUser(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public List<users> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public List<users> getUsers(@Param("name") String name) {
        return userRepository.findByName(name);
    }

    // @Override
    // public LoginResponse authenticateUser(LoginRequest loginRequest) throws Exception {
    //     users user = userRepository.findByEmail(loginRequest.getEmail())
    //             .orElseThrow(() -> new Exception("Invalid email or password"));

    //         if (!loginRequest.getPassword().equals(user.getPsw())) {
    //             throw new Exception("Invalid email or password");
    //         }
    //     //changes made here.
    //     return new LoginResponse(user.getUserId(), user.getEmail(), user.getRole(),"Login successful");
    // }

    

    @Override
    public users findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email " + email + " not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        users user = findByEmail(username);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), 
                user.getPsw(), 
                Collections.singleton(new SimpleGrantedAuthority(user.getRole()))
        );
    }

    @Override
    public List<users> getPendingAdmins() {
        return userRepository.findByRoleAndIsApproved("admin", false);
    }

    @Override
    public Boolean getIsApproved(Long userId) {
        users user = getUser(userId);
        return user.isApproved();
    }

    @Override
    public void approveAdmin(Long userId) {
        users user = getUser(userId);
        user.setApproved(true);
        userRepository.saveAndFlush(user);
    }
    

}
