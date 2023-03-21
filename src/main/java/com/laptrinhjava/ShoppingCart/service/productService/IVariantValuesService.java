package com.laptrinhjava.ShoppingCart.service.productService;

import com.laptrinhjava.ShoppingCart.entity.VariantValues;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IVariantValuesService {
    VariantValues save(VariantValues variantValues);
    List<VariantValues> findById_VariantId(Long variantId);

}
