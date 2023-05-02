package com.laptrinhjava.ShoppingCart.payload.response.dashboard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Top5Users {
    private Long userId;
    private String fullname;
    private String email;
    private Long totalOrder;
    private Double totalPrice;

    public Top5Users(Long userId, String fullname, String email, Long totalOrder, Double totalPrice) {
        this.userId = userId;
        this.fullname = fullname;
        this.email = email;
        this.totalOrder = totalOrder;
        this.totalPrice = totalPrice;
    }
}
