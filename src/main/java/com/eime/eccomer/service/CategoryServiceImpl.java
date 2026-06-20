package com.eime.eccomer.service;

import com.eime.eccomer.exceptions.APIException;
import com.eime.eccomer.exceptions.ResourceNotFoundException;
import com.eime.eccomer.model.Category;
import com.eime.eccomer.payload.CategoryDTO;
import com.eime.eccomer.payload.CategoryResponse;
import com.eime.eccomer.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService{
   // private List<Category> categories = new ArrayList<>();
    private Long nextId = 1L;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending() ;

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories =categoryPage.getContent();
        if(categories.isEmpty())
            throw new APIException("Not category created till now.");
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContente(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalpages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO creteCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO,Category.class);
        Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryFromDB!=null)
            throw new APIException("Category with the name " + category.getCategoryName() + " already exists !!!");
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
        Category category =  modelMapper.map(categoryDTO,Category.class);
        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}

