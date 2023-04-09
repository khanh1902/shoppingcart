package com.laptrinhjava.ShoppingCart.payload.request.product;

import com.laptrinhjava.ShoppingCart.payload.response.product.TypeOptions;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OptionsRequest {
    private Long productId;
    private List<TypeOptions> options;
}
