package com.laptrinhjava.ShoppingCart.service.productService;

import com.laptrinhjava.ShoppingCart.entity.Products;
import com.laptrinhjava.ShoppingCart.payload.request.OptionsRequest;
import com.laptrinhjava.ShoppingCart.payload.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IProductService {
    Products save(Products product);
    Products findProductById(Long id);
    Products findByName(String name);
    void delete(Long id);
    Products update(Products newProduct, Long id);
    Page<ProductResponse> findWithFilterAndPageAndSort (Integer offset, Integer limit, String sortBy, String name);
    List<Map<String, String>> convertOptionsRequestToMap(OptionsRequest optionsRequest);
    List<Products> findByNameContainingIgnoreCase(String name);
    Page<ProductResponse> filterWithPaging(Integer offset, Integer limit, String sortBy, String name, List<Long> categoryIds, Long minPrice, Long maxPrice);

}
