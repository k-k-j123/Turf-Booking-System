package com.turf.turf_booking_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class ContentController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/FindYourTurf")
    public String Home() {
        return "FindYourTurf"; // This will map to src/main/resources/templates/Home.html
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
    
    @GetMapping("/mybookings")
    public String viewBookings() {
        return "mybookings";
    }
    
    @GetMapping("/view_turf")
    public String viewTurfs() {
        return "view_turf";
    }
    
    @GetMapping("/listTurfs")
    public String ListTurfs() {
        return "listTurfs";
    }

    @GetMapping("/bookturf")
    public String bookTurf(){
        return "book_turf";
    }
    
    @GetMapping("/adminPanel")
    public String adminPanel() {
        return "adminPanel";
    }

    @GetMapping("/superadmin")
    public String superAdmin(){
        return "super_admin_panel";
    }
    
}
