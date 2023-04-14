package com.laptrinhjava.ShoppingCart.payload.response.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class OrderItemsResponse {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private String imageUrl;
    private Map<String, Object> option;
    private Long quantity;
    private Double price;
}
