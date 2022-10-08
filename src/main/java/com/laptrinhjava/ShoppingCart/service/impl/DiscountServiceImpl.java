package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.Discount;
import com.laptrinhjava.ShoppingCart.reponsitory.DiscountRepository;
import com.laptrinhjava.ShoppingCart.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl implements DiscountService {
    @Autowired
    private DiscountRepository discountRepository;

    @Override
    public Discount save(Discount discount) {
        return discountRepository.save(discount);
    }

    @Override
    public Discount findDiscountById(Long id) {
        return discountRepository.findDiscountById(id);
    }

    @Override
    public void delete(Long id) {
        discountRepository.deleteById(id);
    }
}
