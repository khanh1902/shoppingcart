package com.laptrinhjava.ShoppingCart.payload.request.reviews;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateReviewRequest {
    private String description;
    private Integer rating;
}
