package com.laptrinhjava.ShoppingCart.payload.response.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private Double price;
    private Long discountPercent;

    public ProductResponse(Long id, String name, String imageUrl, Double price, Long discountPercent) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.discountPercent = discountPercent;
    }
}
