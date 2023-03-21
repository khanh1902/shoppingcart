//package com.laptrinhjava.ShoppingCart.service.impl;
//
//import com.laptrinhjava.ShoppingCart.entity.CartItems;
//import com.laptrinhjava.ShoppingCart.entity.product.Product;
//import com.laptrinhjava.ShoppingCart.reponsitory.ICartItemsRepository;
//import com.laptrinhjava.ShoppingCart.service.ICartItemsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CartItemsServiceImpl implements ICartItemsService {
//    @Autowired
//    private ICartItemsRepository cartItemsRepository;
//
//    @Override
//    public CartItems save(CartItems cartItems) {
//        return cartItemsRepository.save(cartItems);
//    }
//
//    @Override
//    public void deleteByProduct(Product product) {
//        cartItemsRepository.deleteByProduct(product);
//    }
//
//    @Override
//    public void deleteByProductId(Long id) {
//        cartItemsRepository.deleteByProductId(id);
//    }
//}
