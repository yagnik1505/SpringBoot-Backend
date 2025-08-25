package com.project.springproject.Controller;

import com.project.springproject.Entity.Category;
import com.project.springproject.Entity.Task;
import com.project.springproject.Entity.TaskStatus;
import com.project.springproject.Repository.CategoryRepository;
import com.project.springproject.Repository.TaskRepository;
import com.project.springproject.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/tasks")

public class TaskController {

    @Autowired
    private TaskService taskService;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/{categoryId}")
    public Task createTask(@PathVariable Long categoryId, @RequestBody Task task) {
        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            
         
            String username = getLoggedInUsername();
            task.setUsername(username);  
            
            task.setCategory(category);
            return taskRepository.save(task);
        } catch (Exception e) {
            System.err.println("Error creating task: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create task: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Task> getAllTasksForLoggedInUser() {
        try {
            String username = getLoggedInUsername();
            return taskService.getAllTasks(username);
        } catch (Exception e) {
           
            System.err.println("Error fetching tasks: " + e.getMessage());
            e.printStackTrace();
           
            return Collections.emptyList();
        }
    }

    
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        String username = getLoggedInUsername();
        return taskService.getTaskById(id, username);
    }


    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        String username = getLoggedInUsername();
        return taskService.updateTask(id, updatedTask, username);
    }

    
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        String username = getLoggedInUsername();
        taskService.deleteTask(id, username);
        return "Task deleted successfully";
    }

   
    @GetMapping("/status/{status}")
    public List<Task> getTasksByStatus(@PathVariable TaskStatus status) {
        String username = getLoggedInUsername();
        return taskService.getTasksByStatus(username, status);
    }

   
    @GetMapping("/category/{categoryId}")
    public List<Task> getTasksByCategory(@PathVariable Long categoryId) {
        return taskService.getTasksByCategory(categoryId);
    }

  
    @PutMapping("/{id}/complete")
    public Task markTaskAsCompleted(@PathVariable Long id) {
        String username = getLoggedInUsername();
        return taskService.markTaskAsCompleted(id, username);
    }

     private String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); 
    }
}
