package com.laptrinhjava.ShoppingCart.controller.user;

import com.laptrinhjava.ShoppingCart.entity.Category;
import com.laptrinhjava.ShoppingCart.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryUserController {
    @Autowired
    ICategoryService categoryService;

    /**
     * Method: Find All Category
     * **/
    @GetMapping("/findall")
    public List<Category> findAll(){
        return categoryService.findAll();
    }
}
