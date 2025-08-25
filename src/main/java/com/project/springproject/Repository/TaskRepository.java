package com.project.springproject.Repository;

import com.project.springproject.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

   
    List<Task> findAllByCategoryId(Long categoryId);

    List<Task> findByUsernameAndCategoryId(String username, Long categoryId);

	List<Task> findByStatusAndUsername(TaskStatus status, String username);
	List<Task> findByUsername(String username);
	

}