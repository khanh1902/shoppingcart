package com.laptrinhjava.ShoppingCart.payload.response.reviews;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewResponse {
    private Long reviewId;
    private String productName;
    private String imageUrl;
    private Long quantityOrder;
    private Double price;
    private String description;
    private Integer rating;

    public ReviewResponse(Long reviewId, String productName, String imageUrl, Long quantityOrder, Double price, String description, Integer rating) {
        this.reviewId = reviewId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.quantityOrder = quantityOrder;
        this.price = price;
        this.description = description;
        this.rating = rating;
    }
}
