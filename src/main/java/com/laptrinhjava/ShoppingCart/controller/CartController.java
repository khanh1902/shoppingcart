package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.payload.request.cart.CartItemsRequest;
import com.laptrinhjava.ShoppingCart.payload.request.cart.CartRequest;
import com.laptrinhjava.ShoppingCart.payload.request.cart.UpdateCartItemRequest;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.service.ICartItemsService;
import com.laptrinhjava.ShoppingCart.service.ICartService;
import com.laptrinhjava.ShoppingCart.service.IUserService;
import com.laptrinhjava.ShoppingCart.service.productService.IProductService;
import com.laptrinhjava.ShoppingCart.service.productService.IProductVariantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Qualifier("cartServiceImpl")
    @Autowired
    private ICartService cartService;

    @Qualifier("userServiceImpl")
    @Autowired
    private IUserService userService;

    @Qualifier("productsServiceImpl")
    @Autowired
    private IProductService productService;

    @Qualifier("cartItemsServiceImpl")
    @Autowired
    private ICartItemsService cartItemsService;

    @Autowired
    private IProductVariantsService productVariantsService;

    /**
     * method: save cart
     **/
    @PostMapping("/save")
    public ResponseEntity<ResponseObject> save(@RequestBody CartRequest cartRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Save Successfully!", cartService.saveCart(cartRequest))
        );
    }


    /**
     * Method: Find Cart By  cart id
     * Steps: 1: find cart by id form PathVariable
     * 2: save details list of product to CartItemsResponse
     * 3: save CartResponse from Card and CartItemsResponse
     **/
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> findById() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Add Product Successfully!", cartItemsService.getAllCart())
        );

    }

    /**
     * Method: Add 1 Item To Cart
     **/
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> addItems(@RequestBody CartItemsRequest item) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Add Product Successfully!", cartItemsService.addProductToCartItem(item))
        );
    }

    /**
     * Method: Update Cart
     **/
    @PutMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> update(@RequestBody UpdateCartItemRequest item) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Add Product Successfully!", cartItemsService.updateProductInCartItem(item))
        );
    }

    /**
     * Method: Delete All Item
     **/
    @DeleteMapping("/delete-all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> deleteAllItems() {
        cartItemsService.deleteAllCartItems();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Delete Successfully!", "")
        );
    }


    /**
     * Method: Delete 1 Items
     **/
    @DeleteMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> deleteOneItems(@RequestParam(name = "cartItemId") Long carItemId) {
        cartItemsService.deleteOneItemByCartItemId(carItemId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Delete Successfully!", null)
        );
    }
}
