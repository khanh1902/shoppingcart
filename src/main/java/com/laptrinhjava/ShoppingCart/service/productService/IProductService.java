package com.laptrinhjava.ShoppingCart.service.productService;

import com.laptrinhjava.ShoppingCart.entity.Products;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductService {
    List<Products> fillAll();
    Products save(Products product);
    Products findProductById(Long id);
    Products findByName(String name);
    void delete(Long id);
    Products update(Products newProduct, Long id);
}
