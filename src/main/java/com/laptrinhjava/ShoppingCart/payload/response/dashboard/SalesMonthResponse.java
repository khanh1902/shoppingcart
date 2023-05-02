package com.laptrinhjava.ShoppingCart.payload.response.dashboard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SalesMonthResponse {
    private Integer month;
    private Double totalPrice;
    private Long totalProduct;

    public SalesMonthResponse(Integer month, Double totalPrice, Long totalProduct) {
        this.month = month;
        this.totalPrice = totalPrice;
        this.totalProduct = totalProduct;
    }
}
