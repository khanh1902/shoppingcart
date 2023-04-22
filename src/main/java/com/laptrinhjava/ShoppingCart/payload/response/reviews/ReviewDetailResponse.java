package com.laptrinhjava.ShoppingCart.payload.response.reviews;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ReviewDetailResponse {
    private Long reviewId;
    private Integer rating;
    private String description;
    private Date createdDate;
    private String fullName;
    private Boolean isReview;
    private String productName;

    public ReviewDetailResponse(Long reviewId, Integer rating, String description, Date createdDate, String fullName,
                          Boolean isReview, String productName) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.description = description;
        this.createdDate = createdDate;
        this.fullName = fullName;
        this.isReview = isReview;
        this.productName = productName;
    }
}
