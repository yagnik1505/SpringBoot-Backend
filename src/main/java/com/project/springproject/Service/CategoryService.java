package com.project.springproject.Service;
import com.project.springproject.Entity.*;
import com.project.springproject.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private TaskRepository taskRepository;

    public Category createCategory(Category category) {
        // Check if category name already exists
        List<Category> existingCategories = categoryRepository.findAll();
        
        for (Category existingCategory : existingCategories) {
            if (existingCategory.getName().equalsIgnoreCase(category.getName())) {
                throw new RuntimeException("Category with this name already exists");
            }
        }
        
        return categoryRepository.save(category);
    }
    
    public Category updateCategory(Long id, Category updatedCategory) {
        // Find existing category
        Optional<Category> existingCategoryOptional = categoryRepository.findById(id);
        
        if (!existingCategoryOptional.isPresent()) {
            throw new RuntimeException("Category not found with ID: " + id);
        }
        
        Category existingCategory = existingCategoryOptional.get();
        
        // Update category name
        if (updatedCategory.getName() != null) {
            // Check if new name already exists
            List<Category> allCategories = categoryRepository.findAll();
            
            for (Category category : allCategories) {
                if (category.getName().equalsIgnoreCase(updatedCategory.getName()) 
                    && !category.getId().equals(id)) {
                    throw new RuntimeException("Category name already exists");
                }
            }
            
            existingCategory.setName(updatedCategory.getName());
        }
        
        return categoryRepository.save(existingCategory);
    }
    
    public Category getCategoryById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        
        if (!categoryOptional.isPresent()) {
            throw new RuntimeException("Category not found with ID: " + id);
        }
        
        return categoryOptional.get();
    }
    
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    public void deleteCategory(Long categoryId) {
        // Find category first to ensure it exists
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        
        if (!categoryOptional.isPresent()) {
            throw new RuntimeException("Category not found with ID: " + categoryId);
        }
        
        Category category = categoryOptional.get();
        
        // Check if the category has any pending tasks
        List<Task> categoryTasks = taskRepository.findByCategoryId(categoryId);
        
        // If tasks exist, check their status
        if (categoryTasks != null && !categoryTasks.isEmpty()) {
            for (Task task : categoryTasks) {
                if (task.getStatus() != TaskStatus.COMPLETED) {
                    throw new RuntimeException("Cannot delete category. All tasks must be completed first.");
                }
            }
        }
        
        // If no pending tasks, delete the category
        categoryRepository.delete(category);
    }
}