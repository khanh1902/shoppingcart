package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.entity.Cart;
import com.laptrinhjava.ShoppingCart.entity.CartItems;
import com.laptrinhjava.ShoppingCart.entity.Product;
import com.laptrinhjava.ShoppingCart.payload.request.CartItemsRequest;
import com.laptrinhjava.ShoppingCart.payload.request.CartRequest;
import com.laptrinhjava.ShoppingCart.payload.response.CartItemsResponse;
import com.laptrinhjava.ShoppingCart.payload.response.CartResponse;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.service.ICartItemsService;
import com.laptrinhjava.ShoppingCart.service.ICartService;
import com.laptrinhjava.ShoppingCart.service.IProductService;
import com.laptrinhjava.ShoppingCart.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        List<CartItemsResponse> itemsDetails = new ArrayList<>(); // add details list of products saved to cart
        Cart cart = cartService.findById(id);

        if (cart != null) {
            cart.getCartItems().forEach(item -> {
                Product product = productService.findProductById(item.getProduct().getId());

                // save details of product
                CartItemsResponse productDetails = new CartItemsResponse(product.getName(),
                        product.getImageUrl(),
                        item.getQuantity(),
                        item.getPrice());

                itemsDetails.add(productDetails);
            });

            CartResponse cartResponse = new CartResponse(cart.getId(), cart.getUserId(),
                    itemsDetails, cart.getCreatedDate());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Find Successfully!", cartResponse)
            );
        } else
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("FAILED", "Cart Does Not Exist!", null)
            );

    }

    /**
     * Method: Update Cart
     **/
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObject> update(@RequestBody CartRequest cartRequest, @PathVariable Long id) {
        Cart cart = cartService.findById(id);
        List<CartItems> cartItems = new ArrayList<>(); // danh sach san pham them vao gio hang
        List<CartItemsResponse> cartItemsResponses = new ArrayList<>(); // danh sach chi tiet san pham da them vao gio hang
        if (cart != null) {
            try {
                cart.getCartItems().clear(); // remove all items old
                for (CartItemsRequest item : cartRequest.getListProducts()) {

                    CartItems cartItem = new CartItems();

                    Product product = productService.findProductById(item.getProductId());
                    // tao moi exception
                    if (product == null) throw new Exception("Product does not exist!");

                    cartItem.setProduct(product);

                    cartItem.setPrice(product.getPrice() * item.getQuantity());

                    cartItem.setCart(cart);

                    cartItem.setQuantity(item.getQuantity());

                    // them 1 san pham vao danh sach gio hang
                    cartItems.add(cartItem);

                    // thong tin san pham da them vao gio
                    CartItemsResponse response = new CartItemsResponse(product.getName(), product.getImageUrl(),
                            item.getQuantity(), cartItem.getPrice());
                    cartItemsResponses.add(response);
                }

                cart.setCartItems(cartItems); // add new items

                cartService.save(cart);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Update successfully!",
                                new CartResponse(cart.getId(),
                                        cart.getUserId(),
                                        cartItemsResponses,
                                        cart.getCreatedDate()))
                );
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED", "Update failure!", e.getMessage())
                );
            }
        }
        return null;
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
     * Method: Add 1 Item To Cart
     **/
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> addItems(@PathVariable Long id, @RequestBody CartItemsRequest item){
        Cart cart = cartService.findById(id);
        List<CartItems> cartItems = new ArrayList<>(); // danh sach san pham them vao gio hang
        List<CartItemsResponse> cartItemsResponses = new ArrayList<>();
        if (cart != null){
            CartItems cartItem = new CartItems();
            Product product = productService.findProductById(item.getProductId());

            cartItem.setProduct(product);

            cartItem.setQuantity(item.getQuantity());

            cartItem.setPrice(product.getPrice() * item.getQuantity());

            cartItem.setCart(cart);

            cartItems.add(cartItem);

            cart.setCartItems(cartItems); // add new items

            cartService.save(cart);

            // thong tin san pham da them vao gio
            CartItemsResponse response = new CartItemsResponse(product.getName(), product.getImageUrl(),
                    item.getQuantity(), cartItem.getPrice());

            cartItemsResponses.add(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Add Product Successfully!",
                        new CartResponse(cart.getId(),
                                cart.getUserId(),
                                cartItemsResponses,
                                cart.getCreatedDate()))
        );
    }

    /**
     * Method: Delete 1 Items
     **/
    @DeleteMapping("/{id}")
//    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public ResponseEntity<ResponseObject> deleteItems(@PathVariable Long id,
                                                      @RequestParam("productId") Long productId) {
        Cart cart = cartService.findById(id);
        Product product = productService.findProductById(productId);
        if(cart != null){
            for(CartItems c : cart.getCartItems()) {
                if (c.getProduct().equals(product)) {
                    cartItemsService.deleteByProductId(product.getId());
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("OK", "Delete Successfully!", "")
                    );
                }
            }
        }
        return null;
    }
}
