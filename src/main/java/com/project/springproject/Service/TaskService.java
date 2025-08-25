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

    public Task createTask(Task task, String username, Long categoryId) {
        if (!userrepo.existsById(username)) {
            throw new RuntimeException("User with username '" + username + "' not found");
        }

        if (!caterepo.existsById(categoryId)) {
            throw new RuntimeException("Category with ID " + categoryId + " not found");
        }

        task.setUsername(username);
        task.setCategory(caterepo.findById(categoryId).get());
        return taskrepo.save(task);
    }

 
    public List<Task> getAllTasks(String username) {
        if (!userrepo.existsById(username)) {
            throw new RuntimeException("User not found");
        }
        return taskrepo.findByUsername(username);
    }

   
    public Task getTaskById(Long id, String username) {
        if (!taskrepo.existsById(id)) {
            throw new RuntimeException("Task not found");
        }

        Task task = taskrepo.findById(id).get();
        if (!task.getUsername().equals(username)) {
            throw new RuntimeException("Task does not belong to the user");
        }

        return task;
    }


    public Task updateTask(Long id, Task updatetask, String username) {
        if (!taskrepo.existsById(id)) {
            throw new RuntimeException("Task not found");
        }

        Task task = taskrepo.findById(id).get();
        if (!task.getUsername().equals(username)) {
            throw new RuntimeException("Task does not belong to the user");
        }

        task.setTitle(updatetask.getTitle());
        task.setDescription(updatetask.getDescription());
        task.setStatus(updatetask.getStatus());
        task.setDueDate(updatetask.getDueDate());

        return taskrepo.save(task);
    }


    public void deleteTask(Long id, String username) {
        if (!taskrepo.existsById(id)) {
            throw new RuntimeException("Task not found");
        }

        Task task = taskrepo.findById(id).get();
        if (!task.getUsername().equals(username)) {
            throw new RuntimeException("Task does not belong to the user");
        }

        taskrepo.delete(task);
    }


    public List<Task> getTasksByStatus(String username, TaskStatus status) {
        if (!userrepo.existsById(username)) {
            throw new RuntimeException("User not found");
        }
        return taskrepo.findByStatusAndUsername(status, username);
    }


    public List<Task> getTasksByCategory(Long categoryId) {
        if (!caterepo.existsById(categoryId)) {
            throw new RuntimeException("Category not found");
        }
        return taskrepo.findAllByCategoryId(categoryId);
    }


    public Task markTaskAsCompleted(Long taskId, String username) {
        if (!taskrepo.existsById(taskId)) {
            throw new RuntimeException("Task not found");
        }

        Task task = taskrepo.findById(taskId).get();
        if (!task.getUsername().equals(username)) {
            throw new RuntimeException("Task does not belong to the user");
        }

        task.setStatus(TaskStatus.COMPLETED);
        return taskrepo.save(task);
    }

}
