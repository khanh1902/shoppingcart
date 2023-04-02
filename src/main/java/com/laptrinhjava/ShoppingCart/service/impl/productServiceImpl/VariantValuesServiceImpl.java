package com.laptrinhjava.ShoppingCart.service.impl.productServiceImpl;

import com.laptrinhjava.ShoppingCart.entity.VariantValues;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.IVariantValuesRepository;
import com.laptrinhjava.ShoppingCart.service.productService.IVariantValuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VariantValuesServiceImpl implements IVariantValuesService {
    @Autowired
    private IVariantValuesRepository variantValuesRepository;

    @Override
    public VariantValues save(VariantValues variantValues) {
        return variantValuesRepository.save(variantValues);
    }

    @Override
    public List<VariantValues> findById_VariantId(Long variantId) {
        return variantValuesRepository.findById_VariantId(variantId);
    }

    @Override
    public List<VariantValues> findById_ProductId(Long productId) {
        return variantValuesRepository.findById_ProductId(productId);
    }

    @Override
    public void deleteById_ProductId(Long productId) {
        variantValuesRepository.deleteById_ProductId(productId);
    }

    @Override
    public void deleteById_VariantId(Long variantId) {
        variantValuesRepository.deleteById_VariantId(variantId);
    }

    @Override
    public List<VariantValues> findById_ProductIdAndId_OptionIdAndId_ValueId(Long productId, Long optionId, Long valueId) {
        return variantValuesRepository.findById_ProductIdAndId_OptionIdAndId_ValueId(productId, optionId, valueId);
    }
}
