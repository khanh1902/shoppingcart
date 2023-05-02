package com.laptrinhjava.ShoppingCart.payload.response.dashboard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CountProductOrderResponse {
    private Long productId;
    private Long totalQuantity;

    public CountProductOrderResponse(Long productId, Long totalQuantity) {
        this.productId = productId;
        this.totalQuantity = totalQuantity;
    }
}
