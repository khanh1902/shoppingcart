package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.WishList;
import com.laptrinhjava.ShoppingCart.payload.response.WishListsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IWishListService {
    WishList addProductToWishList(Long productId) throws Exception;
    List<WishListsResponse> getAllWishListForUser() throws Exception;
    void DeleteById(Long wishListId) throws Exception;
}
