package com.laptrinhjava.ShoppingCart.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemsResponse {
    private String Name;
    private String url;
    private Long quantity;
    private Long price;

    public CartItemsResponse(String name, String url, Long quantity, Long price) {
        Name = name;
        this.url = url;
        this.quantity = quantity;
        this.price= price;
    }
}
