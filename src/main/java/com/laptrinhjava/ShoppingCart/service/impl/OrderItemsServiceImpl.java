package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.*;
import com.laptrinhjava.ShoppingCart.payload.response.order.OrderItemsResponse;
import com.laptrinhjava.ShoppingCart.reponsitory.ICartItemsRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.IOrderItemsRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.*;
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

    @Autowired
    private IProductVariantsRepository productVariantsRepository;

    public Double discount(Long discountPercent, Double price) {
        if (discountPercent != null) return (Double) price - price * discountPercent / 100L;
        // neu khong co disount percent thi tra ve gia ban dau
        return price;
    }

    @Override
    public List<OrderItemsResponse> addProductsToOrder(Order order, List<Long> cartItemIds) throws Exception {
        List<OrderItemsResponse> orderItemsResponses = new ArrayList<>();
        for(Long cartItemId : cartItemIds){
            CartItems findCartItem = cartItemsRepository.findCartItemsById(cartItemId);
            if(findCartItem != null){
                // luu item vao db
                Products findProduct = productRepository.findProductById(findCartItem.getProductId());
                if(findProduct == null) throw new Exception("Product does not exists!");
                OrderItems orderItem = new OrderItems(order, findCartItem.getProductVariants(), findCartItem.getProductId(),
                         findCartItem.getQuantity(), findCartItem.getQuantity() * discount(findProduct.getDiscountPercent(), findCartItem.getPrice()));
                orderItemsRepository.save(orderItem);
                // cập nhật số lượng sản phẩm trong kho sau khi order
                if(findCartItem.getProductVariants() != null) {
                    ProductVariants findProductVariant = productVariantsRepository.findProductVariantsById(findCartItem.getProductVariants().getId());
                    findProductVariant.setQuantity(findProductVariant.getQuantity() - findCartItem.getQuantity());
                    findProduct.setQuantity(findProduct.getQuantity() - findCartItem.getQuantity());
                    productVariantsRepository.save(findProductVariant);
                    productRepository.save(findProduct);
                }
                else {
                    findProduct.setQuantity(findProduct.getQuantity() - findCartItem.getQuantity());
                    productRepository.save(findProduct);
                }
                OrderItemsResponse orderItemsResponse = new OrderItemsResponse();
                Products product = productRepository.findProductById(orderItem.getProductId());

                orderItemsResponse.setOrderItemId(orderItem.getId());
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
                cartItemsRepository.delete(findCartItem);
            }
        }
        return orderItemsResponses;
    }
}
