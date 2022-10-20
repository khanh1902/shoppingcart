package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.Discount;
import com.laptrinhjava.ShoppingCart.reponsitory.IDiscountRepository;
import com.laptrinhjava.ShoppingCart.service.IDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl implements IDiscountService {
    @Autowired
    private IDiscountRepository discountRepository;

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
