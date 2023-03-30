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
    private String imageUrl;
    private String description;
    private List<Map<String, Object>> options;
    private Long price;
    private Long quantity;

}
