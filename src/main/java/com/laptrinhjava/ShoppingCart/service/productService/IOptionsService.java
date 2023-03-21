package com.laptrinhjava.ShoppingCart.service.productService;

import com.laptrinhjava.ShoppingCart.entity.Options;

import javax.swing.text.html.Option;

public interface IOptionsService {
    Options findById(Long id);
    Options findByName(String name);
}
