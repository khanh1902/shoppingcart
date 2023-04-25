package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ICategoryService {
    List<Category> findAll();
    Category save(Category category);
    Category findCategoryById(Long id);
    void delete(Long id);
    Category update(Category newCategory, Long id);
    Page<Category> findWithPageAndSort(Integer offset, Integer limit, String sortBy);
    Map<String, Object> findWithFilterAndPageAndSort (Integer offset, Integer limit, String sortBy, String name);
}
