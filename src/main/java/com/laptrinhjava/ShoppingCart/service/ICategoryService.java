package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Category;

public interface ICategoryService {
    Category save(Category category);
    Category findCategoryById(Long id);
    void delete(Long id);
}
