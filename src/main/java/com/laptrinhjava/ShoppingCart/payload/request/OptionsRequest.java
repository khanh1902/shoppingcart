package com.laptrinhjava.ShoppingCart.payload.request;

import com.laptrinhjava.ShoppingCart.payload.TypeOptions;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OptionsRequest {
    private Long productId;
    private List<TypeOptions> options;
}
