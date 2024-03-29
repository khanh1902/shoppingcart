package com.laptrinhjava.ShoppingCart.service.productService;

import com.laptrinhjava.ShoppingCart.entity.Products;
import com.laptrinhjava.ShoppingCart.payload.request.product.OptionsRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IProductService {
    List<Products> findAll();
    Products save(Products product);
    Products findProductById(Long id);
    Products findByName(String name);
    List<Products> findByCategory_Id (Long categoryId);
    void delete(Long id);
    Products update(Products newProduct, Long id);
    List<Map<String, String>> convertOptionsRequestToMap(OptionsRequest optionsRequest);
    List<Products> filer(String sortBy, Boolean asc, String name, List<Long> categoryIds, Long minPrice, Long maxPrice);

}
