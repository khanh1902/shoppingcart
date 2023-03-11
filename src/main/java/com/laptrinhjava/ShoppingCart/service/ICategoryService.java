package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICategoryService {
    List<Category> findAll();
    Category save(Category category);
    Category findCategoryById(Long id);
    void delete(Long id);
    Category update(Category newCategory, Long id);
    Page<Category> findAllCategories(Integer offset, Integer limit, String sortBy);
    Page<Category> searchWithFilter(Integer offset, Integer limit, String sortBy, String name);
}
