package com.laptrinhjava.ShoppingCart.payload.response.dashboard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class DashboardResponse {
    private Long totalOrder;
    private Long totalProduct;
    private Double totalPrice;
    private Long orderReceived;
    private Long orderPending;
    private Long orderDelivering;
    private Long orderSuccess;
    private List<Top5Products> top5Products;
    private List<Top5Users> top5Users;

    public DashboardResponse(Long totalOrder, Long totalProduct, Double totalPrice, Long orderReceived, Long orderPending,
                             Long orderDelivering, Long orderSuccess, List<Top5Products> top5Products,
                             List<Top5Users> top5Users) {
        this.totalOrder = totalOrder;
        this.totalProduct = totalProduct;
        this.totalPrice = totalPrice;
        this.orderReceived = orderReceived;
        this.orderPending = orderPending;
        this.orderDelivering = orderDelivering;
        this.orderSuccess = orderSuccess;
        this.top5Products = top5Products;
        this.top5Users = top5Users;
    }
}
