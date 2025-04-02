package com.project.springproject.Security;

import com.project.springproject.Entity.Task;
import com.project.springproject.Entity.User;
import com.project.springproject.Repository.TaskRepository;
import com.project.springproject.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaskSecurity {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskSecurity(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // Ensure user can update their own profile only
    public boolean isProfileOwner(Long userId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String currentUsername = authentication.getName();
        Optional<User> user = userRepository.findById(userId);

        return user.map(value -> value.getUsername().equals(currentUsername)).orElse(false);
    }

    // Ensure user can update only their own tasks
    public boolean isTaskOwner(Long taskId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String currentUsername = authentication.getName();
        Optional<Task> task = taskRepository.findById(taskId);

        return task.map(value -> value.getUser().getUsername().equals(currentUsername)).orElse(false);
    }
}
