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
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request, HttpServletRequest httpRequest) {
        String message = authService.authenticateUser(request.getUsername(), request.getPassword());

        HttpSession session = httpRequest.getSession(true); // Create new session
        session.setAttribute("username", request.getUsername());

        return ResponseEntity.ok("Login successful! Session ID: " + session.getId());
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
            return ResponseEntity.ok("Logged in as: " + authentication.getName());
        }
        return ResponseEntity.status(401).body("Not authenticated");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthRequest request) {
        try {
            authService.registerUser(request.getUsername(), request.getPassword());
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
