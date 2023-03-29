package com.laptrinhjava.ShoppingCart.service.impl.productServiceImpl;

import com.laptrinhjava.ShoppingCart.entity.ProductOptions;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.IProductOptionsRepository;
import com.laptrinhjava.ShoppingCart.service.productService.IProductOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductOptionsServiceImpl implements IProductOptionsService {
    @Autowired
    private IProductOptionsRepository productOptionsRepository;

    @Override
    public ProductOptions save(ProductOptions productOptions) {
        return productOptionsRepository.save(productOptions);
    }

    @Override
    public List<ProductOptions> findByProducts_Id(Long productId) {
        return productOptionsRepository.findByProducts_Id(productId);
    }

    @Override
    public void deleteById_ProductId(Long productId) {
        productOptionsRepository.deleteById_ProductId(productId);
    }
}
