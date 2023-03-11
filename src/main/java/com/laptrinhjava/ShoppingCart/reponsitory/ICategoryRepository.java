package com.laptrinhjava.ShoppingCart.reponsitory;

import com.amazonaws.services.cognitoidp.model.DeviceType;
import com.laptrinhjava.ShoppingCart.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long>
        , JpaSpecificationExecutor<DeviceType> {
    Category findCategoryById(Long id);

//    @Query("SELECT c FROM category c WHERE CONCAT(c.name, c.code, c.imageurl, c.createddate) LIKE %?1%")
    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Category> findByNameContainingIgnoreCase(String name);
}
