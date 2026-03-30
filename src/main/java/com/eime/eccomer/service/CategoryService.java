package com.eime.eccomer.service;

import com.eime.eccomer.exceptions.ResourceNotFoundException;
import com.eime.eccomer.model.Category;
import com.eime.eccomer.payload.CategoryDTO;
import com.eime.eccomer.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO creteCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long categoryId);
    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) throws ResourceNotFoundException;
}
