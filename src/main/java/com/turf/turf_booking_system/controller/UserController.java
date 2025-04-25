    package com.turf.turf_booking_system.controller;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    import org.springframework.beans.factory.annotation.Autowired;

    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.web.bind.annotation.CookieValue;
    import org.springframework.web.bind.annotation.DeleteMapping;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestHeader;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import com.turf.turf_booking_system.Security.utillis.JwtUtil;
    import com.turf.turf_booking_system.dto.LoginRequest;
    import com.turf.turf_booking_system.model.users;
    import com.turf.turf_booking_system.service.userService;

    import jakarta.servlet.http.Cookie;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;

    import org.springframework.web.bind.annotation.PutMapping;
    import org.springframework.web.bind.annotation.RequestParam;



    @RestController
    @RequestMapping("api/users")
    public class UserController {
        

        users manager;
        @Autowired
        private JwtUtil jwtUtil;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        userService userservice;
        public UserController(userService uService){
            this.userservice=uService;
        }

        //for Specific User based on ID.
        @PreAuthorize("hasRole('customer') or hasRole('admin') or hasRole('super_admin')")
        @GetMapping("{user_Id}")
        public users getUserDetails(@PathVariable("user_Id") Long user_Id) {
            return userservice.getUser(user_Id);
        }

        //for specific user based on Name.
        @PreAuthorize("hasRole('admin') or hasRole('super_admin')")
        @GetMapping("/search")
        public ResponseEntity<List<users>> searchUsers(@RequestParam String name) {
            List<users> users = userservice.getUsers(name);
            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(users);
        }

        //for All users in Databse.
        @PreAuthorize("hasRole('super_admin') or hasRole('customer')")
        @GetMapping
        public List<users> getAllUsers() {
            return userservice.getAllUser();
        }

        //To create new Users
        @PostMapping(value = "/reg/signup", consumes = "application/json")
        public ResponseEntity<String> createUser(@RequestBody users user) {
            userservice.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully.");
        }
        
        //changes made here
        @PostMapping("/login")
        public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest,HttpServletResponse response) {
            // Log the incoming request
            System.out.println("Login request received for email: " + loginRequest.getEmail());
            
            try {
                // Authenticate the user
                Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                    )
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
    
                // Get the authenticated user's email
                String email = authentication.getName();
    
                // Fetch the user from the database
                users user = userservice.findByEmail(email);
    
                if ("admin".equals(user.getRole()) && !user.isApproved()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin account not approved");
                }
                // Generate JWT token
                String jwtToken = jwtUtil.generateToken(user.getUserId(),user.getRole());
    
    
                // return ResponseEntity.ok(loginResponse);
                // Create the cookie
                Cookie jwtCookie = new Cookie("token", jwtToken);
                jwtCookie.setHttpOnly(true);      // Prevent JavaScript access
                // jwtCookie.setSecure(true);       // Send only over HTTPS
                jwtCookie.setPath("/");          // Available to all paths
                jwtCookie.setMaxAge(24 * 60 * 60); // 1 day expiration

                // Add the cookie to the response
                response.addCookie(jwtCookie);

                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("message", "Login successful");
                
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseBody);
    
            } catch (BadCredentialsException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid email or password");
            } catch (Exception e) {
                e.printStackTrace(); // Log the exception for debugging
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("An error occurred during login");
            }
        }

        //logout api
        @PostMapping("/logout")
        public ResponseEntity<String> logout(HttpServletResponse response, @CookieValue(name = "token", required = false) String token) {
            // Clear the token cookie
            Cookie cookie = new Cookie("token", null);
            cookie.setHttpOnly(true);
            // cookie.setSecure(true); // Use true if your app uses HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(0); // Set Max-Age to 0 to delete the cookie
            response.addCookie(cookie);

            return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
        }

        //To Update Users.
        @PreAuthorize("hasRole('customer') or hasRole('super_admin')")
        @PutMapping
        public String updateUser(@RequestBody users user) {
            userservice.updateUser(user);
            return "Sucessfully Updated the User.";
        }

        //To Delete a User
        @PreAuthorize("hasRole('super_admin')")
        @DeleteMapping("{user_Id}")
        public String deleteUser(@PathVariable("user_Id") Long user_Id) {
            userservice.delete(user_Id);
            return "Sucessfully Deleted a User with User-ID: "+user_Id;
        } 
        
        //to Validate the token
        @GetMapping("/validateToken")
        public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                Map<String,Boolean> response = new HashMap<>();
                response.put("isValid",false);
                return ResponseEntity.ok(response);
            }

            String token = authorizationHeader.substring(7); // Extract the token (remove "Bearer ")
            try {
                if (jwtUtil.validateToken(token)) {
                    Map<String,Boolean> response = new HashMap<>();
                    response.put("isValid",true);
                    return ResponseEntity.ok(response);
                } else {
                    Map<String,Boolean> response = new HashMap<>();
                    response.put("isValid",false);
                    return ResponseEntity.ok(response);
                }
            } catch (Exception e) {
                Map<String,Boolean> response = new HashMap<>();
                response.put("isValid",false);
                return ResponseEntity.ok(response);
            }
        }

        //fetching user role
        @GetMapping("/get-user-role")
        public ResponseEntity<?> getUserRole(HttpServletRequest request) {
            String token = jwtUtil.extractTokenFromCookie(request);
            String role = jwtUtil.extractRole(token);
            Map<String, String> response = new HashMap<>();
            response.put("role", role);
            return ResponseEntity.ok(response);
        }

        //Remember this there can issue here
        @PreAuthorize("hasRole('customer') or hasRole('admin')")
        @GetMapping("/me")
        public ResponseEntity<?> getLoggedInUser(HttpServletRequest request) {
            String token = jwtUtil.extractTokenFromCookie(request);
            String userId = jwtUtil.extractUsername(token);
            Long userid = Long.parseLong(userId);
            manager = userservice.getUser(userid);
            Map<String, String> response = new HashMap<>();
            response.put("user_id", userId);
            return ResponseEntity.ok(response);
        }

        public users getManagerLoggedIn(){
            return manager;
        }

        @GetMapping("/api/admins/pending")
        public ResponseEntity<List<users>> getPendingAdmins() {
            List<users> pendingAdmins = userservice.getPendingAdmins();
            return ResponseEntity.ok(pendingAdmins);
        }
        
        @PutMapping("/api/admins/approve/{userId}")
        public ResponseEntity<String> approveAdmin(@PathVariable Long userId) {
            userservice.approveAdmin(userId);
            return ResponseEntity.ok("Admin approved successfully");
        }

        @GetMapping("/api/users/{userId}/is-approved")
        public ResponseEntity<Boolean> getIsApproved(@PathVariable Long userId) {
            Boolean isApproved = userservice.getIsApproved(userId);
            return ResponseEntity.ok(isApproved);
        }

    }
