package com.laptrinhjava.ShoppingCart.payload.response.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderForUserResponse {
    private Long orderId;
    private Long totalItems;
    private Double totalPrice;

    public OrderForUserResponse(Long orderId, Long totalItems, Double totalPrice) {
        this.orderId = orderId;
        this.totalItems = totalItems;
        this.totalPrice = totalPrice;
    }
}
