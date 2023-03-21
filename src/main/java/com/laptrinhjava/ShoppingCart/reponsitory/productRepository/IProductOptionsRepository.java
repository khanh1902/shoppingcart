package com.laptrinhjava.ShoppingCart.reponsitory.productRepository;

import com.laptrinhjava.ShoppingCart.entity.ProductOptions;
import com.laptrinhjava.ShoppingCart.entity.ProductOptionsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductOptionsRepository extends JpaRepository<ProductOptions, ProductOptionsKey> {
//    ProductOptions findByOptionId(Long optionId);
//    ProductOptions findProductOptionsByProductId(Long productId);
}
