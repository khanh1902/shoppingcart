package com.laptrinhjava.ShoppingCart.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TypeOptions {
    private String key;
    private List<String> values;
}
