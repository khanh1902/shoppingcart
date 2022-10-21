package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Discount;

import java.util.List;

public interface IDiscountService {
    List<Discount> findAll();
    Discount save(Discount discount);
    Discount findDiscountById(Long id);
    void delete(Long id);
    Discount update(Discount newDiscount, Long id);
}
