package com.laptrinhjava.ShoppingCart.service.productService;

import com.laptrinhjava.ShoppingCart.entity.ProductOptions;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductOptionsService {
    ProductOptions save(ProductOptions productOptions);
    List<ProductOptions> findByProducts_Id(Long productId);
    void deleteById_ProductId(Long productId);

}
