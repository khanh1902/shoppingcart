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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private ICartService cartService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IProductService productService;
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
     *        2: save details list of product to CartItemsResponse
     *        3: save CartResponse from Card and CartItemsResponse
     **/
    @GetMapping
    public ResponseEntity<ResponseObject> findById(@RequestParam(name = "userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Add Product Successfully!", cartItemsService.getAllCart(userId))
        );

    }
    /**
     * Method: Add 1 Item To Cart
     **/
    @PostMapping
    public ResponseEntity<ResponseObject> addItems(@RequestParam(name = "userId") Long userId,
                                                   @RequestBody CartItemsRequest item) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Add Product Successfully!", cartItemsService.addProductToCartItem(userId, item))
        );
    }

    /**
     * Method: Update Cart
     **/
    @PutMapping
    public ResponseEntity<ResponseObject> update(@RequestParam(name = "userId") Long userId,
                                                 @RequestBody UpdateCartItemRequest item) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Add Product Successfully!", cartItemsService.updateProductInCartItem(userId, item))
        );
    }

    /**
     * Method: Delete Cart
     **/
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ResponseObject> deleteById(@PathVariable Long id) {
//        Cart cart = cartService.findById(id);
//        if (cart != null) {
//            cartService.deleteById(id);
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponseObject("OK", "Delete Successfully!", "")
//            );
//        } else
//            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
//                    new ResponseObject("FAILED", "Delete Does Not Successfully!", "")
//            );
//    }




/**
 * Method: Delete 1 Items
 **/
//    @DeleteMapping("/{id}")
////    @Transactional(rollbackFor = {Exception.class, Throwable.class})
//    public ResponseEntity<ResponseObject> deleteItems(@PathVariable Long id,
//                                                      @RequestParam("productId") Long productId) {
//        Cart cart = cartService.findById(id);
//        Product product = productService.findProductById(productId);
//        if(cart != null){
//            for(CartItems c : cart.getCartItems()) {
//                if (c.getProduct().equals(product)) {
//                    cartItemsService.deleteByProductId(product.getId());
//                    return ResponseEntity.status(HttpStatus.OK).body(
//                            new ResponseObject("OK", "Delete Successfully!", "")
//                    );
//                }
//            }
//        }
//        return null;
//    }
}
