package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductService {
    List<Product> fillAll();
    Product save(Product product);
    Product findProductById(Long id);
    Product findByName(String name);
    void delete(Long id);
}
