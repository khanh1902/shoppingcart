package com.laptrinhjava.ShoppingCart.payload.response.order;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateStatusResponse {
    private Long orderId;
    private String newStatus;
}
