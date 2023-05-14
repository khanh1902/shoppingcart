package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.payload.ResponseObject;
import com.laptrinhjava.ShoppingCart.service.IWishListService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {
    @Qualifier("wishListServiceImpl")
    @Autowired
    private IWishListService wishListService;

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addProductToWishList(@RequestParam(name = "productId") Long productId){
        try {
            wishListService.addProductToWishList(productId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Add Successfully!", null)
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED", e.getMessage(), null)
            );
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<ResponseObject> getALlProductInWishList(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Get Successfully!", wishListService.getAllWishListForUser())
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED", e.getMessage(), null)
            );
        }
    }

    @DeleteMapping
    public ResponseEntity<ResponseObject> deleteByWishlistId(@RequestParam(name = "wishlistId") Long wishlistId){
        try {
            wishListService.DeleteById(wishlistId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Delete Successfully!", null)
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED", e.getMessage(), null)
            );
        }
    }
}
