package com.laptrinhjava.ShoppingCart.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class CartRequest {
    private ArrayList<CartItemsRequest> listProducts;

    public CartRequest() {
    }
}
