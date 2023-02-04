package com.laptrinhjava.ShoppingCart.controller.user;

import com.laptrinhjava.ShoppingCart.entity.Product;
import com.laptrinhjava.ShoppingCart.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductUserController {
    @Autowired
    private IProductService productService;

    /**
     * Method: Find All Product
     * **/
    @GetMapping("/findAll")
    public List<Product> findAll(){
        return productService.fillAll();
    }
}
