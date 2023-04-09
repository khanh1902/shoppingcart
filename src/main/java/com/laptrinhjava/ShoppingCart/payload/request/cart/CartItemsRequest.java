package com.laptrinhjava.ShoppingCart.payload.request.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class CartItemsRequest {
    private Long productId;
    private Map<String, Object> option;
    private Long quantity;

    public CartItemsRequest(Long productId, Map<String, Object> option, Long quantity) {
        this.productId = productId;
        this.option = option;
        this.quantity = quantity;
    }
}
