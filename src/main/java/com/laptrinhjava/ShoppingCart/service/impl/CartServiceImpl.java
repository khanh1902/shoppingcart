package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.Cart;
import com.laptrinhjava.ShoppingCart.entity.CartItems;
import com.laptrinhjava.ShoppingCart.entity.Product;
import com.laptrinhjava.ShoppingCart.entity.User;
import com.laptrinhjava.ShoppingCart.payload.request.CartItemsRequest;
import com.laptrinhjava.ShoppingCart.payload.request.CartRequest;
import com.laptrinhjava.ShoppingCart.payload.response.CartItemsResponse;
import com.laptrinhjava.ShoppingCart.payload.response.CartResponse;
import com.laptrinhjava.ShoppingCart.reponsitory.ICartRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.IProductRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.IUserRepository;
import com.laptrinhjava.ShoppingCart.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProductRepository productRepository;

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public CartResponse saveCart(CartRequest cartRequest) {
        List<CartItems> cartItems = new ArrayList<>(); // danh sach san pham them vao gio hang
        List<CartItemsResponse> cartItemsResponses = new ArrayList<>(); // danh sach chi tiet san pham da them vao gio hang
        try {
            Cart cart = new Cart();
            cart.setId(getUserId());
            cart.setUserId(getUserId());
            for (CartItemsRequest item : cartRequest.getListProducts()) {

                CartItems cartItem = new CartItems();

                Product product = productRepository.findProductById(item.getProductId());
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

            cart.setCartItems(cartItems);
            cartRepository.save(cart);
            return new CartResponse(cart.getId(),
                            cart.getUserId(),
                            cartItemsResponses,
                            cart.getCreatedDate());
        } catch (Exception e) {
            return null;
        }
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User findByUserName = userRepository.findByEmail(email);
        if (findByUserName != null)
            return findByUserName.getId();
        else
            return null;
    }

    @Override
    public Cart findById(Long id) {
        return cartRepository.findCartById(id);
    }

    @Override
    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }
}
