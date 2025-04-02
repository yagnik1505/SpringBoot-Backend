package com.project.springproject.Controller;

import com.project.springproject.Entity.Role;
import com.project.springproject.Entity.User;
import com.project.springproject.Repository.UserRepository;
import com.project.springproject.Service.AuthService;
import com.project.springproject.dto.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request, HttpServletRequest httpRequest) {
        String message = authService.authenticateUser(request.getUsername(), request.getPassword());

        HttpSession session = httpRequest.getSession(true); // Create new session
        session.setAttribute("username", request.getUsername());

        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        String role = optionalUser.isPresent() ? optionalUser.get().getRole().name() : "UNKNOWN";

        return ResponseEntity.ok("Login successful! Session ID: " + session.getId() + ", Role: " + role);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Retrieve session if exists
        if (session != null) {
            session.invalidate(); // Invalidate session
        }
        return ResponseEntity.ok("Logout successful!");
    }

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<User> optionalUser = userRepository.findByUsername(authentication.getName());

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                return ResponseEntity.ok("Logged in as: " + user.getUsername() +
                                        ", ID: " + user.getId() +
                                        ", Role: " + user.getRole());
            } else {
                return ResponseEntity.status(404).body("User not found");
            }
        }
        return ResponseEntity.status(401).body("Not authenticated");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthRequest request) {
        try {
            authService.registerUser(request.getUsername(), request.getPassword());
            return ResponseEntity.ok("User registered successfully with role USER");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/admin/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AuthRequest request) {
        try {
            // Check if user exists
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body("User already exists!");
            }

            // Create admin user manually
            User adminUser = new User();
            adminUser.setUsername(request.getUsername());
            adminUser.setPassword(passwordEncoder.encode(request.getPassword()));
            adminUser.setRole(Role.ADMIN);
            userRepository.save(adminUser);

            return ResponseEntity.ok("Admin user registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
