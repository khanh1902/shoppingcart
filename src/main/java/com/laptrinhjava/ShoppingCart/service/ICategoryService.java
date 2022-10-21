package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> findAll();
    Category save(Category category);
    Category findCategoryById(Long id);
    void delete(Long id);
    Category update(Category newCategory, Long id);
}
