package com.laptrinhjava.ShoppingCart.payload.response.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TypeOptions {
    private String key;
    private List<String> values;
}
