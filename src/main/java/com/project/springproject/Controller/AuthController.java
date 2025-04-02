package com.project.springproject.Controller;

import com.project.springproject.Service.AuthService;
import com.project.springproject.dto.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

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
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            return ResponseEntity.ok("Logged in as: " + session.getAttribute("username"));
        }
        return ResponseEntity.status(401).body("Not authenticated");
    }
}
