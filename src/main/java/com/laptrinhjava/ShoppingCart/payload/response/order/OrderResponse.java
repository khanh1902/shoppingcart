package com.laptrinhjava.ShoppingCart.payload.response.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Long userId;
    private String email;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String status;
    private List<OrderItemsResponse> orderItemsResponses;
    private Double totalPrice;

    public OrderResponse(Long orderId, Long userId, String email, String fullName, String address, String phoneNumber, String status,
                         List<OrderItemsResponse> orderItemsResponses, Double totalPrice) {
        this.orderId = orderId;
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.orderItemsResponses = orderItemsResponses;
        this.totalPrice = totalPrice;
    }
}
