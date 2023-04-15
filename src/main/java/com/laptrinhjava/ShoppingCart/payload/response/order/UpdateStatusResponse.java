package com.laptrinhjava.ShoppingCart.payload.response.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UpdateStatusResponse {
    private Long orderId;
    private String newStatus;

    public UpdateStatusResponse(Long orderId, String newStatus) {
        this.orderId = orderId;
        this.newStatus = newStatus;
    }
}
