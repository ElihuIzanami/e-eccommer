package com.eime.eccomer.service;

import com.eime.eccomer.exceptions.ResourceNotFoundException;
import com.eime.eccomer.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();
    void creteCategory(Category category);
    String deleteCategory(Long categoryId) throws ResourceNotFoundException;
    Category updateCategory(Category category, Long categoryId) throws ResourceNotFoundException;
}
