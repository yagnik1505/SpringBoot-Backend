package com.project.springproject.Repository;

import com.project.springproject.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatusAndUserId(TaskStatus status, Long userId);
    List<Task> findByUserId(Long userId);
    Optional<Task> findByIdAndUserId(Long id, Long userId);
    List<Task> findByCategoryId(Long categoryId);
    List<Task> findAllByCategoryId(Long categoryId);
    
    // New method to find tasks by user and category
    List<Task> findByUser_IdAndCategory_Id(Long userId, Long categoryId);

    List<Task> findByUserIdAndCategoryId(Long userId, Long categoryId);
}