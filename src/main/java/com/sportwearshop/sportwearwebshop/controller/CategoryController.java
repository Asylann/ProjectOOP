package com.sportwearshop.sportwearwebshop.controller;

import com.sportwearshop.sportwearwebshop.entity.Category;
import com.sportwearshop.sportwearwebshop.entity.Product;
import com.sportwearshop.sportwearwebshop.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable int id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id:" + id));
    }
    @PostMapping
    public Category addCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable int id, @RequestBody Category updatedCategory){
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(updatedCategory.getName());
                    category.setDescription(updatedCategory.getDescription());
                    return categoryRepository.save(category);
                }).orElseThrow(()-> new RuntimeException("Category not found with id "+id));
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable int id){
        if (categoryRepository.existsById(id)){
            categoryRepository.deleteById(id);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found");
        }
    }
}

