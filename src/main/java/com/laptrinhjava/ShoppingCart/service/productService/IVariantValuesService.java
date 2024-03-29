package com.laptrinhjava.ShoppingCart.service.productService;

import com.laptrinhjava.ShoppingCart.entity.VariantValues;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IVariantValuesService {
    VariantValues save(VariantValues variantValues);
    List<VariantValues> findById_VariantId(Long variantId);
    List<VariantValues> findById_ProductId(Long productId);
    void deleteById_ProductId(Long productId);
    void deleteById_VariantId(Long variantId);
    List<VariantValues> findById_ProductIdAndId_OptionIdAndId_ValueId(Long productId, Long optionId, Long valueId);



}
