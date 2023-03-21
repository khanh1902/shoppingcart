package com.laptrinhjava.ShoppingCart.reponsitory.productRepository;

import com.laptrinhjava.ShoppingCart.entity.VariantValues;
import com.laptrinhjava.ShoppingCart.entity.VariantValuesKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVariantValuesRepository extends JpaRepository<VariantValues, VariantValuesKey> {
    List<VariantValues> findById_VariantId(Long variantId);
}
