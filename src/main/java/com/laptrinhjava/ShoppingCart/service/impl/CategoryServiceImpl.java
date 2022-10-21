package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.Category;
import com.laptrinhjava.ShoppingCart.reponsitory.ICategoryRepository;
import com.laptrinhjava.ShoppingCart.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
        return categoryRepository.findById(id).map(
                category -> {
                    category.setCode(newCategory.getCode());
                    category.setName(newCategory.getName());
                    category.setCreatedDate(new Date());
                    return categoryRepository.save(category);
                }).orElseGet(() -> {
                    newCategory.setId(id);
                    return categoryRepository.save(newCategory);
                }
        );
    }
}
