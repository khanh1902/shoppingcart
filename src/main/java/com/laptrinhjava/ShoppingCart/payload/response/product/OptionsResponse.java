package com.laptrinhjava.ShoppingCart.payload.response.product;

import com.laptrinhjava.ShoppingCart.entity.Products;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OptionsResponse {
    private Products products;
}
