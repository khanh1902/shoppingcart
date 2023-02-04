package com.laptrinhjava.ShoppingCart.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemsRequest {
    private Long productId;
    private Long quantity;

    public CartItemsRequest() {
    }

    public CartItemsRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
