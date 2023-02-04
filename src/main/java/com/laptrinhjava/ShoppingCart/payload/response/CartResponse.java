package com.laptrinhjava.ShoppingCart.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CartResponse {
    private Long id;
    private Long userId;
    private List<CartItemsResponse> cartItems;
    private Date createdDate;

    public CartResponse(Long id, Long userId, List<CartItemsResponse> cartItems, Date createdDate) {
        this.id = id;
        this.userId = userId;
        this.cartItems = cartItems;
        this.createdDate = createdDate;
    }
}
