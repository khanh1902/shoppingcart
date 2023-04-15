package com.laptrinhjava.ShoppingCart.payload.response.cart;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartResponse {
    private Long id;
    private Long userId;
    private List<CartItemsResponse> cartItems;

    public CartResponse(Long id, Long userId, List<CartItemsResponse> cartItems) {
        this.id = id;
        this.userId = userId;
        this.cartItems = cartItems;
    }
}
