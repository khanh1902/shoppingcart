package com.laptrinhjava.ShoppingCart.reponsitory.productRepository;


import com.laptrinhjava.ShoppingCart.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Products, Long> {
    Products findProductById (Long id);
    Products findByName (String name);
    Page<Products> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Products> findByIsDeleteAndNameContainingIgnoreCase(Boolean isDelete, String name, Sort sort);
    List<Products> findAllByIsDelete(Boolean isDelete, Sort sort);
    @Transactional
    @Modifying
    List<Products> findByCategory_Id(Long categoryId);
}
