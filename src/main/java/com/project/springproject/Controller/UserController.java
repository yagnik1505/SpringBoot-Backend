package com.project.springproject.Controller;

import com.project.springproject.Entity.*;
import com.project.springproject.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // User Registration (Public)
    @PostMapping()
    public User createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
        return userService.createUser(user);
    }

    // User can update only their own profile
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser, Authentication authentication) {
        Optional<User> userOptional = Optional.of(userService.getUserById(id));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.getUsername().equals(authentication.getName())) {
                return ResponseEntity.status(403).body("You can only update your own profile.");
            }
            user.setUsername(updatedUser.getUsername());
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // Encrypt new password
            userService.updateUser(id, user);
            return ResponseEntity.ok("Profile updated successfully.");
        }
        return ResponseEntity.status(404).body("User not found.");
    }

    // User can retrieve only their own profile
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id, Authentication authentication) {
        Optional<User> userOptional = Optional.of(userService.getUserById(id));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.getUsername().equals(authentication.getName()) &&
                !authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                return ResponseEntity.status(403).body("Access denied.");
            }
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(404).body("User not found.");
    }

    // Only ADMIN can view all users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.ok(userService.getAllUsers());
        }
        return ResponseEntity.status(403).body(null);
    }

    // Only ADMIN can delete users
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully.");
        }
        return ResponseEntity.status(403).body("Only ADMIN can delete users.");
    }
}
