package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.*;
import com.laptrinhjava.ShoppingCart.payload.response.cart.CartItemsResponse;
import com.laptrinhjava.ShoppingCart.payload.response.order.OrderItemsResponse;
import com.laptrinhjava.ShoppingCart.reponsitory.ICartItemsRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.IOrderItemsRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.IOptionValuesRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.IOptionsRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.IProductRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.IVariantValuesRepository;
import com.laptrinhjava.ShoppingCart.service.productService.IOrderItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.laptrinhjava.ShoppingCart.common.HandleChar.upperFirstString;
@Service
public class OrderItemsServiceImpl implements IOrderItemsService {

    @Autowired
    private ICartItemsRepository cartItemsRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IVariantValuesRepository variantValuesRepository;

    @Autowired
    private IOptionsRepository optionsRepository;

    @Autowired
    private IOptionValuesRepository optionValuesRepository;

    @Autowired
    private IOrderItemsRepository orderItemsRepository;

    @Override
    public List<OrderItemsResponse> addProductsToOrder(Order order, List<Long> cartItemIds) {
        List<OrderItemsResponse> orderItemsResponses = new ArrayList<>();
        for(Long cartItemId : cartItemIds){
            CartItems findCartItem = cartItemsRepository.findCartItemsById(cartItemId);
            if(findCartItem != null){
                // luu item vao db
                OrderItems orderItem = new OrderItems(order, findCartItem.getProductVariants(), findCartItem.getProductId(), findCartItem.getQuantity(), findCartItem.getPrice());
                orderItemsRepository.save(orderItem);
                OrderItemsResponse orderItemsResponse = new OrderItemsResponse();
                Products product = productRepository.findProductById(orderItem.getProductId());

                orderItemsResponse.setCartItemId(orderItem.getId());
                orderItemsResponse.setProductId(orderItem.getProductId());
                orderItemsResponse.setQuantity(orderItem.getQuantity());
                orderItemsResponse.setPrice(orderItem.getPrice());
                orderItemsResponse.setImageUrl(product.getImageUrl());
                orderItemsResponse.setProductName(product.getName());

                // nếu product không có option => option null
                if (findCartItem.getProductVariants() == null) {
                    orderItemsResponse.setOption(null);
                } else {
                    Map<String, Object> option = new HashMap<>();
                    List<VariantValues> variantValues = variantValuesRepository.findById_VariantId(orderItem.getProductVariants().getId());
                    for (VariantValues variantValue : variantValues) {
                        Options findOption = optionsRepository.findOptionById(variantValue.getId().getOptionId());
                        OptionValues findOptionValue = optionValuesRepository.findOptionValuesById(variantValue.getId().getValueId());
                        option.put(upperFirstString(findOption.getName().toLowerCase()), upperFirstString(findOptionValue.getName().toLowerCase()));
                    }
                    orderItemsResponse.setOption(option);
                }
                orderItemsResponses.add(orderItemsResponse);
            }
        }
        return orderItemsResponses;
    }
}