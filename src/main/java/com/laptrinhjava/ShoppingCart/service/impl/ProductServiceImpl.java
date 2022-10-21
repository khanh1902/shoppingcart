package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.Discount;
import com.laptrinhjava.ShoppingCart.entity.Product;
import com.laptrinhjava.ShoppingCart.reponsitory.IProductRepository;
import com.laptrinhjava.ShoppingCart.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Override
    public List<Product> fillAll() {
        return productRepository.findAll();
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.findProductById(id);
    }

    @Override
    public Product findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product update(Product newProduct, Long id) {
        return productRepository.findById(id).map(
                product -> {
                    product.setName(newProduct.getName());
                    product.setPrice(newProduct.getPrice());
                    product.setImageUrl(newProduct.getImageUrl());
                    product.setCategoryId(newProduct.getCategoryId());
                    product.setDiscountId(newProduct.getDiscountId());
                    product.setCreatedDate(new Date());
                    return productRepository.save(product);
                }
        ).orElseGet(() -> {
            newProduct.setId(id);
            return productRepository.save(newProduct);
        });
    }
}
