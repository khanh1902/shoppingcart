package com.laptrinhjava.ShoppingCart.payload.response.dashboard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Top5Products {
    private Long productId;
    private String imageUrl;
    private String productName;
    private Long totalSales;

    public Top5Products(Long productId, String imageUrl, String productName, Long totalSales) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.totalSales = totalSales;
    }
}
