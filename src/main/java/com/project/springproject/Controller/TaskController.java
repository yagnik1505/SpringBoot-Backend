package com.project.springproject.Controller;

import com.project.springproject.Entity.*;

import com.project.springproject.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping(value = "/{userId}/{categoryId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Task createTask(
            @PathVariable Long userId, 
            @PathVariable Long categoryId, 
            @RequestBody Task task) {
        return taskService.createTask(task, userId, categoryId);
    }

    @GetMapping("/user/{userId}")
    public List<Task> getAllTasksForUser(@PathVariable Long userId) {
        return taskService.getAllTasks(userId);
    }

    @GetMapping("/{id}/user/{userId}")
    public Task getTaskById(@PathVariable Long id, @PathVariable Long userId) {
        return taskService.getTaskById(id, userId);
    }

    @PutMapping("/{id}/user/{userId}")
    public Task updateTask(
            @PathVariable Long id, 
            @PathVariable Long userId, 
            @RequestBody Task updatedTask) {
        return taskService.updateTask(id, updatedTask, userId);
    }

    

    @DeleteMapping("/{id}/user/{userId}")
    public String deleteTask(@PathVariable Long id, @PathVariable Long userId) {
        taskService.deleteTask(id, userId);
        return "Task deleted successfully";
    }

    @GetMapping("/user/{userId}/status/{status}")
    public List<Task> getTasksByStatus(
            @PathVariable Long userId, 
            @PathVariable TaskStatus status) {
        return taskService.getTasksByStatus(userId, status);
    }

    @GetMapping("/category/{categoryId}")
    public List<Task> getTasksByCategory(@PathVariable Long categoryId) {
        return taskService.getTasksByCategory(categoryId);
    }

    @PutMapping("/{id}/user/{userId}/complete")
        public Task markTaskAsCompleted(@PathVariable Long id, @PathVariable Long userId) {
        return taskService.markTaskAsCompleted(id, userId);
    }
}