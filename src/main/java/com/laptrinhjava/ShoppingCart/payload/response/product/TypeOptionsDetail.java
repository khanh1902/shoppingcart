package com.laptrinhjava.ShoppingCart.payload.response.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TypeOptionsDetail {
    private String size;
    private String color;
    private Long price;
    private Long quantity;

    public TypeOptionsDetail(String size, String color, Long price, Long quantity) {
        this.size = size;
        this.color = color;
        this.price = price;
        this.quantity = quantity;
    }
}
