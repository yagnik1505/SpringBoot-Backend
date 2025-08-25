package com.project.springproject.Entity;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TaskStatus status;

	@Column(nullable = false)
	private String dueDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	@JsonBackReference("category-tasks")
	private Category category;

	@Column(name = "username", nullable = false)
	private String username;
	
	// Constructor
	public Task() {
	}

	public Task(String title, String description, TaskStatus status, String dueDate, Category category,
			String username) {
		this.title = title;
		this.description = description;
		this.status = status;
		this.dueDate = dueDate;
		this.category = category;
		this.username = username;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category2) {
		this.category = category2;
	}

	public String getUsername() {  
	    return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", title=" + title + ", description=" + description + ", status=" + status
				+ ", dueDate=" + dueDate + ", username=" + username + "]";
	}
}
