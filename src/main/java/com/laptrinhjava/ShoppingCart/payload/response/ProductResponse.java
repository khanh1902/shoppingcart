package com.laptrinhjava.ShoppingCart.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String imageURL;
    private Double price;

    public ProductResponse(Long id, String name, String imageURL, Double price) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
        this.price = price;
    }
}
