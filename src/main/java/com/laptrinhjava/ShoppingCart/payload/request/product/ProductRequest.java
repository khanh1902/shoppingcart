package com.laptrinhjava.ShoppingCart.payload.request.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private Long price;
    private Long categoryId;
    private Long discountId;

    public ProductRequest() {
    }

    public ProductRequest(String name, Long price, Long categoryId, Long discountId) {
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.discountId = discountId;
    }
}
