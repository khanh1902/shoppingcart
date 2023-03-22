package com.laptrinhjava.ShoppingCart.reponsitory;

import com.laptrinhjava.ShoppingCart.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryById(Long id);
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
    @Query("SELECT b FROM Category b WHERE LOWER(b.name) like CONCAT('%',LOWER(?1),'%')")
    List<Category> findByNameLike(String name);
}
