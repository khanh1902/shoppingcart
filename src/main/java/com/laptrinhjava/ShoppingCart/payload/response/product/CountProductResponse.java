package com.laptrinhjava.ShoppingCart.payload.response.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CountProductResponse {
    private Long productId;
    private Long countProduct;

    public CountProductResponse(Long productId, Long countProduct) {
        this.productId = productId;
        this.countProduct = countProduct;
    }
}
