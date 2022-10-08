package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Discount;

public interface DiscountService {
    Discount save(Discount discount);
    Discount findDiscountById(Long id);
    void delete(Long id);
}
