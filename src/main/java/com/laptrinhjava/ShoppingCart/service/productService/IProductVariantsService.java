package com.laptrinhjava.ShoppingCart.service.productService;

import com.laptrinhjava.ShoppingCart.entity.ProductVariants;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductVariantsService {
    ProductVariants save(ProductVariants productVariants);
    List<ProductVariants> findByProducts_Id(Long productId);
    ProductVariants findBySkuId(String skuId);
}
