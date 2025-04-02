package com.project.springproject.Service;

import com.project.springproject.Entity.*;
import com.project.springproject.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskrepo;
    
    @Autowired 
    private UserRepository userrepo;
    
    @Autowired
    private CategoryRepository caterepo;

    public Task createTask(Task task, Long userId, Long categoryId) {
        Optional<User> userobj = userrepo.findById(userId);
        if (!userobj.isPresent()) {
            throw new RuntimeException("User with ID " + userId + " not found");
        }
        User user = userobj.get();

        Optional<Category> categoryobj = caterepo.findById(categoryId);
        if (!categoryobj.isPresent()) {
            throw new RuntimeException("Category with ID " + categoryId + " not found");
        }
        Category category = categoryobj.get();
        
        task.setUser(user);
        task.setCategory(category);

        return taskrepo.save(task);
    }

    public List<Task> getAllTasks(Long userId) {
        if (!userrepo.existsById(userId)) {
            throw new RuntimeException("User not created yet.");
        }
        
        return taskrepo.findByUserId(userId);
    }

    public Task getTaskById(Long id, Long userId) {
        Optional<Task> taskOptional = taskrepo.findByIdAndUserId(id, userId);
        
        if (!taskOptional.isPresent()) {
            throw new RuntimeException("Task not exist for given user ");
        }
        
        return taskOptional.get();
    }

    public Task updateTask(Long id, Task updatetask, Long userid) {
        Optional<Task> taskobj = taskrepo.findByIdAndUserId(id, userid);
        
        if (!taskobj.isPresent()) {
            throw new RuntimeException("Task not exist for given user");
        }
        
        Task task = taskobj.get();
        
        task.setTitle(updatetask.getTitle());
        task.setDescription(updatetask.getDescription());
        task.setStatus(updatetask.getStatus());
        task.setDueDate(updatetask.getDueDate());
        
        return taskrepo.save(task);
    }

    public void deleteTask(Long id, Long userId) {
        Optional<Task> task = taskrepo.findByIdAndUserId(id, userId);
        
        if (!task.isPresent()) {
            throw new RuntimeException("Task not exist for given user");
        }
        
        taskrepo.delete(task.get());
    }

    public List<Task> getTasksByStatus(Long userId, TaskStatus status) {
        if (!userrepo.existsById(userId)) {
            throw new RuntimeException("user not exist");
        }
        
        return taskrepo.findByStatusAndUserId(status, userId);
    }
    
    public List<Task> getTasksByCategory(Long categoryId) {
        if(!caterepo.existsById(categoryId)) {
            throw new RuntimeException("category doesnot exist");
        }
        
        return taskrepo.findAllByCategoryId(categoryId);
    }
    
    public Task markTaskAsCompleted(Long taskId, Long userId) {
        Optional<Task> taskOptional = taskrepo.findByIdAndUserId(taskId, userId);
        
        if (!taskOptional.isPresent()) {
            throw new RuntimeException("Task not found or does not belong to user");
        }
        
        Task task = taskOptional.get();
        task.setStatus(TaskStatus.COMPLETED);
        
        return taskrepo.save(task);
    }
}

