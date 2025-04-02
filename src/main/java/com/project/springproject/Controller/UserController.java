package com.project.springproject.Controller;

import com.project.springproject.Entity.*;
import com.project.springproject.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // Public endpoint for user registration
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // User can update only their own profile
    @PreAuthorize("hasAnyRole('USER', 'ADMIN') and (authentication.name == @userService.getUserById(#id).username or hasRole('ADMIN'))")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser, Authentication authentication) {
        userService.updateUser(id, updatedUser);
        return ResponseEntity.ok("Profile updated successfully.");
    }

    // User can retrieve only their own profile or admin can access any profile
    @PreAuthorize("hasAnyRole('USER', 'ADMIN') and (authentication.name == @userService.getUserById(#id).username or hasRole('ADMIN'))")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id, Authentication authentication) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Only ADMIN can view all users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Only ADMIN can delete users
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
