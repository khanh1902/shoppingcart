package com.laptrinhjava.ShoppingCart.reponsitory.productRepository;

import com.laptrinhjava.ShoppingCart.entity.ProductVariants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductVariantsRepository extends JpaRepository<ProductVariants, Long> {
    ProductVariants findProductVariantsById(Long id);
    List<ProductVariants> findByProducts_Id(Long productId);
    ProductVariants findBySkuId(String skuId);
    void deleteById(Long id);
}
