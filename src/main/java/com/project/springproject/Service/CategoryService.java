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
        
        List<Category> existingCategories = categoryRepository.findAll();
        
        for (Category existingCategory : existingCategories) {
            if (existingCategory.getName().equalsIgnoreCase(category.getName())) {
                throw new RuntimeException("Category with this name already exists");
            }
        }
        
        return categoryRepository.save(category);
    }
    
    public Category updateCategory(Long id, Category updatedCategory) {
        
        Optional<Category> ex = categoryRepository.findById(id);
        
        if (!ex.isPresent()) {
            throw new RuntimeException("Category not found with ID: " + id);
        }
        
        Category existingCategory = ex.get();
        
        if (updatedCategory.getName() != null) {
        
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
       
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        
        if (!categoryOptional.isPresent()) {
            throw new RuntimeException("Category not found with ID: " + categoryId);
        }
        
        Category category = categoryOptional.get();
        
      
        List<Task> categoryTasks = taskRepository.findAllByCategoryId(categoryId);
        
       
        if (categoryTasks != null && !categoryTasks.isEmpty()) {
            for (Task task : categoryTasks) {
                if (task.getStatus() != TaskStatus.COMPLETED) {
                    throw new RuntimeException("Cannot delete category. All tasks must be completed first.");
                }
            }
        }
        
       
        categoryRepository.delete(category);
    }
}