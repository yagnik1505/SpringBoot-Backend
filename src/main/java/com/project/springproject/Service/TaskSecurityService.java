package com.project.springproject.Service;

import com.project.springproject.Entity.*;
import com.project.springproject.Repository.TaskRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TaskSecurityService {

    private final TaskRepository taskRepository;

    public TaskSecurityService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public boolean isTaskOwner(Long taskId, Authentication authentication) {
        return taskRepository.findById(taskId)
                .filter(task -> task.getUser().getUsername().equals(authentication.getName())) // User must own the task
                .isPresent();
    }
}
