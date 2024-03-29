package com.laptrinhjava.ShoppingCart.payload.response.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class OneProductResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private Long categoryId;
    private List<Map<String, Object>> options;
    private Double price;
    private Long quantity;
    private Long discountPercent;
    private Double averageRating;
    private Long countReviews;

}
