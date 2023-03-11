package com.laptrinhjava.ShoppingCart.reponsitory;

import com.laptrinhjava.ShoppingCart.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryById(Long id);

//    @Query("SELECT c FROM category c WHERE CONCAT(c.name, c.code, c.imageurl, c.createddate) LIKE %?1%")
    Page<Category> findByNameContaining(String name, Pageable pageable);
}
