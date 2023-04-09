package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.CartItems;
import com.laptrinhjava.ShoppingCart.payload.request.cart.CartItemsRequest;
import com.laptrinhjava.ShoppingCart.payload.request.cart.UpdateCartItemRequest;
import com.laptrinhjava.ShoppingCart.payload.response.cart.CartResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICartItemsService {
    CartItems save(CartItems cartItems);
//    void deleteByProduct(Product product);
    void deleteByProductId(Long id);
    CartItems findByProductIdAndProductVariantsId(Long productId, Long productVariantsId);
    List<CartItems> findByProductId(Long productId);
    CartItems addProductToCartItem(Long cartId, CartItemsRequest item);
    CartResponse getAllCart(Long cartId);
    CartItems updateProductInCartItem(Long cartId, UpdateCartItemRequest item);
}
