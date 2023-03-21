package com.laptrinhjava.ShoppingCart.reponsitory.productRepository;

import com.laptrinhjava.ShoppingCart.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOptionsRepository extends JpaRepository<Options, Long> {
    Options findOptionById(Long id);
    Options findByName(String name);
}
