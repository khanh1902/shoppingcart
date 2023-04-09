package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.*;
import com.laptrinhjava.ShoppingCart.payload.request.cart.CartItemsRequest;
import com.laptrinhjava.ShoppingCart.payload.request.cart.UpdateCartItemRequest;
import com.laptrinhjava.ShoppingCart.payload.response.cart.CartItemsResponse;
import com.laptrinhjava.ShoppingCart.payload.response.cart.CartResponse;
import com.laptrinhjava.ShoppingCart.reponsitory.ICartItemsRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.ICartRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.*;
import com.laptrinhjava.ShoppingCart.service.ICartItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.laptrinhjava.ShoppingCart.common.HandleChar.upperFirstString;

@Service
public class CartItemsServiceImpl implements ICartItemsService {
    @Autowired
    private ICartItemsRepository cartItemsRepository;

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IProductVariantsRepository productVariantsRepository;

    @Autowired
    private IVariantValuesRepository variantValuesRepository;

    @Autowired
    private IOptionsRepository optionsRepository;

    @Autowired
    private IOptionValuesRepository optionValuesRepository;

    @Override
    public CartItems save(CartItems cartItems) {
        return cartItemsRepository.save(cartItems);
    }

    @Override
    public void deleteByProductId(Long id) {

    }

    @Override
    public CartItems findByProductIdAndProductVariantsId(Long productId, Long productVariantsId) {
        return cartItemsRepository.findByProductIdAndProductVariantsId(productId, productVariantsId);
    }

    @Override
    public List<CartItems> findByProductId(Long productId) {
        return cartItemsRepository.findByProductId(productId);
    }

    @Override
    public CartItems addProductToCartItem(Long userId, CartItemsRequest item) {
        Cart cart = cartRepository.findByUserId(userId);
        Products product = productRepository.findProductById(item.getProductId());
        StringBuilder skuid = new StringBuilder();
        skuid.append("P").append(product.getId());
        for (Map.Entry<String, Object> option : item.getOption().entrySet()) {
            skuid.append(option.getKey().toUpperCase().charAt(0)).append(option.getValue().toString().toUpperCase());
        }
        List<CartItems> cartItemsList = cartItemsRepository.findByProductId(product.getId());
        CartItems cartItems = null;
        if (!cartItemsList.isEmpty()) {
            //nếu sản phẩm đã tồn tại trong giỏ hàng thì update lại số lượng
            for (CartItems cartItem : cartItemsList) {
                if (cartItem.getProductVariants() == null) {
                    cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                    cartItem.setPrice(cartItem.getPrice() + (item.getQuantity() * product.getPrice()));
                    cartItemsRepository.save(cartItem);
                    cart.setTotalPrice(cart.getTotalPrice() + (item.getQuantity() * product.getPrice()));
                    cartRepository.save(cart);
                    return cartItem;
                } else if (cartItem.getProductVariants().getSkuId().equals(skuid.toString())) {
                    cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                    cartItem.setPrice(cartItem.getPrice() + (item.getQuantity() * product.getPrice()));
                    cartItemsRepository.save(cartItem);
                    cart.setTotalPrice(cart.getTotalPrice() + (item.getQuantity() * product.getPrice()));
                    cartRepository.save(cart);
                    return cartItem;
                }
            }
            // nếu sản phẩm chưa tồn tại trong giỏ hàng thì lưu mới
        } else {
            ProductVariants productVariant = productVariantsRepository.findBySkuId(skuid.toString());
            if (productVariant != null) {
                cartItems = new CartItems(cart, product.getId(), item.getQuantity(), productVariant.getPrice() * item.getQuantity(), productVariant);
            } else {
                cartItems = new CartItems(cart, product.getId(), item.getQuantity(), product.getPrice() * item.getQuantity(), null);
            }
            cartItemsRepository.save(cartItems);

            cart.setTotalPrice(cart.getTotalPrice() + (item.getQuantity() * product.getPrice()));
            cartRepository.save(cart);
            return cartItems;
        }
        return null;
    }

    @Override
    public CartResponse getAllCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        List<CartItemsResponse> cartItems = new ArrayList<>();
        if (cart != null) {
            List<CartItems> findCartItemsByCartId = cartItemsRepository.findByCart_Id(cart.getId());
            for (CartItems cartItem : findCartItemsByCartId) {
                CartItemsResponse cartItemsResponse = new CartItemsResponse();
                Products product = productRepository.findProductById(cartItem.getProductId());
                cartItemsResponse.setProductName(product.getName());
                cartItemsResponse.setImageUrl(product.getImageUrl());
                cartItemsResponse.setPrice(cartItem.getPrice());
                cartItemsResponse.setQuantity(cartItem.getQuantity());

                // nếu product không có option => option null
                if (cartItem.getProductVariants() == null) {
                    cartItemsResponse.setOption(null);
                } else {
                    Map<String, Object> option = new HashMap<>();
                    List<VariantValues> variantValues = variantValuesRepository.findById_VariantId(cartItem.getProductVariants().getId());
                    for (VariantValues variantValue : variantValues) {
                        Options findOption = optionsRepository.findOptionById(variantValue.getId().getOptionId());
                        OptionValues findOptionValue = optionValuesRepository.findOptionValuesById(variantValue.getId().getValueId());
                        option.put(upperFirstString(findOption.getName().toLowerCase()), upperFirstString(findOptionValue.getName().toLowerCase()));
                    }
                    cartItemsResponse.setOption(option);
                }
                // lưu danh sách các product vào listCartItems
                cartItems.add(cartItemsResponse);

            }

            return new CartResponse(cart.getId(), cart.getUserId(), cartItems, cart.getTotalPrice());
        } else
            return null;
    }

    @Override
    public CartItems updateProductInCartItem(Long userId, UpdateCartItemRequest item) {
        Cart findCart = cartRepository.findByUserId(userId);
        if (findCart != null) {
            Double totalPrice = findCart.getTotalPrice();
            CartItems findCartItem = cartItemsRepository.findCartItemsById(item.getCartItemId());
            Products findProduct = productRepository.findProductById(findCartItem.getProductId());
            if (findCartItem.getProductVariants() == null) {
                if (item.getAddQuantity() != null) {
                    findCartItem.setQuantity(findCartItem.getQuantity() + item.getAddQuantity());
                    findCartItem.setPrice(findCartItem.getPrice() + (findProduct.getPrice() * item.getAddQuantity()));
                    totalPrice += findProduct.getPrice() * item.getAddQuantity();
                } else if (item.getSubQuantity() != null) {
                    findCartItem.setQuantity(findCartItem.getQuantity() - item.getSubQuantity());
                    findCartItem.setPrice(findCartItem.getPrice() - (findProduct.getPrice() * item.getSubQuantity()));
                    totalPrice -= findProduct.getPrice() * item.getAddQuantity();
                }
                cartItemsRepository.save(findCartItem);
                return findCartItem;
            } else {
                StringBuilder skuid = new StringBuilder();
                skuid.append("P").append(findProduct.getId());
                for (Map.Entry<String, Object> option : item.getOption().entrySet()) {
                    skuid.append(option.getKey().toUpperCase().charAt(0)).append(option.getValue().toString().toUpperCase());
                }
                ProductVariants findProductVariant = productVariantsRepository.findBySkuId(skuid.toString());

                // nếu thay đổi option thì cập nhật lại productVariant thông qua skuid
                if (!findProductVariant.getSkuId().equals(findCartItem.getProductVariants().getSkuId())) {
                    totalPrice -= findCartItem.getPrice();
                    findCartItem.setProductVariants(findProductVariant);
                    findCartItem.setPrice(findCartItem.getQuantity() * findProductVariant.getPrice());
                    if (item.getAddQuantity() != null) {
                        findCartItem.setQuantity(findCartItem.getQuantity() + item.getAddQuantity());
                        //cập nhật lại số tiền cho option mới
                        findCartItem.setPrice(findCartItem.getQuantity() * findProductVariant.getPrice());
                        findCart.setTotalPrice(findCart.getTotalPrice() + item.getAddQuantity() * findProductVariant.getPrice());
                    } else if (item.getSubQuantity() != null) {
                        findCartItem.setQuantity(findCartItem.getQuantity() - item.getSubQuantity());
                        findCartItem.setPrice(findCartItem.getQuantity() * findProductVariant.getPrice());
                    }
                    cartItemsRepository.save(findCartItem);
                    totalPrice += findCartItem.getPrice();
//                    return findCartItem;
                } else {
                    if (item.getAddQuantity() != null) {
                        findCartItem.setQuantity(findCartItem.getQuantity() + item.getAddQuantity());
                        findCartItem.setPrice(findCartItem.getPrice() + (findProductVariant.getPrice() * item.getAddQuantity()));
                        totalPrice += findProductVariant.getPrice() * item.getAddQuantity();
                    } else if (item.getSubQuantity() != null) {
                        findCartItem.setQuantity(findCartItem.getQuantity() - item.getSubQuantity());
                        findCartItem.setPrice(findCartItem.getPrice() - (findProductVariant.getPrice() * item.getSubQuantity()));
                        totalPrice -= findProductVariant.getPrice() * item.getSubQuantity();
                    }
                    cartItemsRepository.save(findCartItem);
//                    return findCartItem;
                }
                findCart.setTotalPrice(totalPrice);
                cartRepository.save(findCart);
            }
            return findCartItem;
        }

        return null;
    }


//    @Override
//    public void deleteByProduct(Product product) {
//        cartItemsRepository.deleteByProduct(product);
//    }
//
//    @Override
//    public void deleteByProductId(Long id) {
//        cartItemsRepository.deleteByProductId(id);
//    }
}
