package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Cart;
import com.laptrinhjava.ShoppingCart.entity.CartItems;
import org.springframework.stereotype.Service;

@Service
public interface ICartItemsService {
    CartItems save (CartItems cartItems);
    void deleteByCart(Cart cart);
}
