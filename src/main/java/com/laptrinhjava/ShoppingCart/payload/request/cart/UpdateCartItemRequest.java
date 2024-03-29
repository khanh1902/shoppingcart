package com.laptrinhjava.ShoppingCart.payload.request.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class UpdateCartItemRequest {
    private Long cartItemId;
    private Long productId;
    private Map<String, Object> option;
//    private Long addQuantity;
//    private Long subQuantity;
    private Long quantity;

    public UpdateCartItemRequest(Long cartItemId, Long productId, Map<String, Object> option, Long quantity) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.option = option;
        this.quantity = quantity;
    }
}
