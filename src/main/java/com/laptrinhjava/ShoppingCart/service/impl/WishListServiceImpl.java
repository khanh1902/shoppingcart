package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.Products;
import com.laptrinhjava.ShoppingCart.entity.Users;
import com.laptrinhjava.ShoppingCart.entity.WishList;
import com.laptrinhjava.ShoppingCart.payload.response.WishListsResponse;
import com.laptrinhjava.ShoppingCart.reponsitory.IUserRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.IWishListRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.IProductRepository;
import com.laptrinhjava.ShoppingCart.service.IWishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.laptrinhjava.ShoppingCart.common.HandleAuth.getUsername;

@Service
public class WishListServiceImpl implements IWishListService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IWishListRepository wishListRepository;

    @Override
    public WishList addProductToWishList(Long productId) throws Exception {
        String email = getUsername();
        Users findUser = userRepository.findByEmail(email);
        Products findProduct = productRepository.findProductById(productId);
        if(findProduct == null) throw new Exception("Product does not exists!");

        // kiem tra xem san pham da duoc them vao wishlist chua
        WishList checkWishList = wishListRepository.findByUsers_IdAndProducts_Id(findUser.getId(), findProduct.getId());
        if(checkWishList != null) throw new Exception("This product has been added to Your WishList!");


        WishList wishList = new WishList(findProduct, findUser);
        return wishListRepository.save(wishList);
    }

    @Override
    public List<WishListsResponse> getAllWishListForUser() throws Exception {
        String email = getUsername();
        Users findUser = userRepository.findByEmail(email);
        List<WishListsResponse> wishListsResponses = new ArrayList<>();
        List<WishList> findAllByUserId = wishListRepository.findAllByUsers_Id(findUser.getId());
        if (findAllByUserId.isEmpty()) throw new Exception("Your Wishlist is Empty!");
        for (WishList wishList:findAllByUserId){
            Products findProduct = productRepository.findProductById(wishList.getProducts().getId());
            WishListsResponse wishListsResponse = new WishListsResponse(wishList.getId(), findProduct.getId(), findProduct.getImageUrl(), findProduct.getPrice());
            wishListsResponses.add(wishListsResponse);
        }
        return wishListsResponses;
    }

    @Override
    public void DeleteById(Long wishListId) throws Exception {
        String email = getUsername();
        Users findUser = userRepository.findByEmail(email);
        WishList findWishList = wishListRepository.findByIdAndUsers_Id(wishListId, findUser.getId());
        if(findWishList == null) throw new Exception("WishList does not exists!");
        wishListRepository.delete(findWishList);
    }
}
