package com.laptrinhjava.ShoppingCart.service.impl.productServiceImpl;

import com.laptrinhjava.ShoppingCart.entity.ProductVariants;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.IProductVariantsRepository;
import com.laptrinhjava.ShoppingCart.service.productService.IProductVariantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariantsService implements IProductVariantsService {
    @Autowired
    private IProductVariantsRepository productVariantsRepository;

    @Override
    public ProductVariants save(ProductVariants productVariants) {
        return productVariantsRepository.save(productVariants);
    }

    @Override
    public List<ProductVariants> findByProducts_Id(Long productId) {
        return productVariantsRepository.findByProducts_Id(productId);
    }

    @Override
    public ProductVariants findBySkuId(String skuId) {
        return productVariantsRepository.findBySkuId(skuId);
    }
}
