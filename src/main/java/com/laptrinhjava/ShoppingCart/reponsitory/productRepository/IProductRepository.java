package com.laptrinhjava.ShoppingCart.reponsitory.productRepository;


import com.laptrinhjava.ShoppingCart.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Products, Long> {
    Products findProductById(Long id);
    Products findByName(String name);
}
