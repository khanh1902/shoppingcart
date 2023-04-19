package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.Category;
import com.laptrinhjava.ShoppingCart.reponsitory.ICategoryRepository;
import com.laptrinhjava.ShoppingCart.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category findCategoryById(Long id) {
        return categoryRepository.findCategoryById(id);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category update(Category newCategory, Long id) {

        Category category = categoryRepository.findCategoryById(id);
        if (category != null) {
            if (!newCategory.getName().isEmpty()) category.setName(newCategory.getName());
            else category.setName(category.getName());

            if (!newCategory.getCode().isEmpty()) category.setCode(newCategory.getCode());
            else category.setCode(category.getCode());

            if (!newCategory.getImageUrl().isEmpty()) category.setImageUrl(newCategory.getImageUrl());
            else category.setImageUrl(category.getImageUrl());

            categoryRepository.save(category);
        } else {
            newCategory.setId(id);
            categoryRepository.save(newCategory);
        }
        return categoryRepository.findCategoryById(id);
    }

    @Override
    public Page<Category> findWithPageAndSort(Integer offset, Integer limit, String sortBy) {
        Pageable paging = PageRequest.of(offset, limit, Sort.by(sortBy).descending()); // sort giảm dần

        Page<Category> pagedResult = categoryRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult;
        } else
            return null;
    }


    @Override
    public Map<String, Object> findWithFilterAndPageAndSort(Integer offset, Integer limit, String sortBy, String name) {
        try {
            Pageable paging = PageRequest.of(offset, limit, Sort.by(sortBy).descending());
            Page<Category> pagedResult = null;
            if (name == null) {
                pagedResult = categoryRepository.findAll(paging);
            } else {
                pagedResult = categoryRepository.findByNameContainingIgnoreCase(name, paging);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("categories", pagedResult);
            response.put("currentPage", Optional.of(pagedResult.getNumber()));
            response.put("totalItems", Optional.of(pagedResult.getTotalElements()));
            response.put("totalPages", Optional.of(pagedResult.getTotalPages()));
            return response;
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("Error", e.getMessage());
            return error;
        }
    }
}
