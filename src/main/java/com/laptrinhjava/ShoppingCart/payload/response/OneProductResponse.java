package com.laptrinhjava.ShoppingCart.payload.response;

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
    private String imageURL;
    private String description;
    private Long categoryId;
    private List<Map<String, Object>> options;
    private Double price;
    private Long quantity;

}
