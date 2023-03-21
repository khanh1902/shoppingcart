package com.laptrinhjava.ShoppingCart.payload;

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

    public TypeOptionsDetail(String size, String color, Long price) {
        this.size = size;
        this.color = color;
        this.price = price;
    }
}
