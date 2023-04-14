package com.laptrinhjava.ShoppingCart.payload.request.order;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateStatusRequest {
    private Long orderId;
    private String newStatus;
}
