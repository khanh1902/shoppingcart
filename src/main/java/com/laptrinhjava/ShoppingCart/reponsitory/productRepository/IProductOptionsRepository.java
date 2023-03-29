package com.laptrinhjava.ShoppingCart.reponsitory.productRepository;

import com.laptrinhjava.ShoppingCart.entity.ProductOptions;
import com.laptrinhjava.ShoppingCart.entity.ProductOptionsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IProductOptionsRepository extends JpaRepository<ProductOptions, ProductOptionsKey> {
    List<ProductOptions> findByProducts_Id(Long productId);
    @Transactional
    @Modifying
    void deleteById_ProductId(Long productId);
}
