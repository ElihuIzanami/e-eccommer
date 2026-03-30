package com.eime.eccomer.controller;

import com.eime.eccomer.config.AppConstants;
import com.eime.eccomer.model.Category;
import com.eime.eccomer.payload.CategoryDTO;
import com.eime.eccomer.payload.CategoryResponse;
import com.eime.eccomer.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder   ) {
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categoryResponse,HttpStatus.OK);
    }

    @PostMapping("public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = categoryService.creteCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable  Long categoryId) {
        CategoryDTO deletedCategory = categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(deletedCategory);
    }

    @PutMapping("public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId){
        CategoryDTO savedCategory = categoryService.updateCategory(categoryDTO,categoryId);
        return new ResponseEntity<CategoryDTO>(savedCategory, HttpStatus.OK);
    }


}
