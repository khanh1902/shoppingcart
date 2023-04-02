package com.laptrinhjava.ShoppingCart.reponsitory.productRepository;

import com.laptrinhjava.ShoppingCart.entity.VariantValues;
import com.laptrinhjava.ShoppingCart.entity.VariantValuesKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IVariantValuesRepository extends JpaRepository<VariantValues, VariantValuesKey> {
    List<VariantValues> findById_VariantId(Long variantId);
    List<VariantValues> findById_ProductId(Long productId);
    @Transactional
    @Modifying
    void deleteById_ProductId(Long productId);
    @Transactional
    @Modifying
    void deleteById_VariantId(Long variantId);
    List<VariantValues> findById_ProductIdAndId_OptionIdAndId_ValueId(Long productId, Long optionId, Long valueId);
}
