package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Cart;
import com.laptrinhjava.ShoppingCart.payload.request.cart.CartRequest;
import com.laptrinhjava.ShoppingCart.payload.response.cart.CartResponse;
import org.springframework.stereotype.Service;

@Service
public interface ICartService {
    Cart save(Cart cart);
    Cart findById(Long id);
    void deleteById(Long id);
}
