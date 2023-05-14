package com.laptrinhjava.ShoppingCart.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishListsResponse {
    private Long wishListId;
    private Long productId;
    private String imageUrl;
    private Double productPrice;
}
