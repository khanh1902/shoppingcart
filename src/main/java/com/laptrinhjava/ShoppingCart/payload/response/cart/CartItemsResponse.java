package com.laptrinhjava.ShoppingCart.payload.response.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class CartItemsResponse {
    private Long productId;
    private String productName;
    private String imageUrl;
    private Map<String, Object> option;
    private Long quantity;
    private Double price;

    public CartItemsResponse(Long productId, String productName, String imageUrl, Map<String, Object> option, Long quantity, Double price) {
        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.option = option;
        this.quantity = quantity;
        this.price = price;
    }
}
