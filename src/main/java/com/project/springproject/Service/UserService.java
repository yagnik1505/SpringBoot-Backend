package com.project.springproject.Service;
import com.project.springproject.Entity.*;

import com.project.springproject.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TaskRepository taskRepository;

    public User createUser(User user) {
        // Check if username already exists
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        return userRepository.save(user);
    }
    
    public User updateUser(Long id, User updatedUser) {
        // Find existing user
        Optional<User> existingUserOptional = userRepository.findById(id);
        
        if (!existingUserOptional.isPresent()) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        
        User existingUser = existingUserOptional.get();
        
        // Update username if provided and not already taken
        if (updatedUser.getUsername() != null) {
            Optional<User> userWithSameUsername = userRepository.findByUsername(updatedUser.getUsername());
            
            if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(id)) {
                throw new RuntimeException("Username already exists");
            }
            
            existingUser.setUsername(updatedUser.getUsername());
        }
        
        return userRepository.save(existingUser);
    }
    
    public User getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        
        if (!userOptional.isPresent()) {
            System.err.println("User not Found");
            throw new RuntimeException("User not found with ID: " + id);
        }
        
        return userOptional.get();
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public void deleteUser(Long userId) {
        // Find user first to ensure it exists
        Optional<User> userOptional = userRepository.findById(userId);
        
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        
        User user = userOptional.get();
        
        // Check if the user has any pending tasks
        List<Task> userTasks = taskRepository.findByUserId(userId);
        
        // If tasks exist, check their status
        if (userTasks != null && !userTasks.isEmpty()) {
            for (Task task : userTasks) {
                if (task.getStatus() != TaskStatus.COMPLETED) {
                    throw new RuntimeException("Cannot delete user. All tasks must be completed first.");
                }
            }
        }
        
        // If no pending tasks, delete the user
        userRepository.delete(user);
    }
}