package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Order;
import com.laptrinhjava.ShoppingCart.entity.OrderItems;
import com.laptrinhjava.ShoppingCart.payload.response.order.OrderItemsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderItemsService {
    List<OrderItems> findAll();
    List<OrderItemsResponse> addProductsToOrder(Order order, List<Long> cartItemId) throws Exception;
    List<OrderItems> findByOrder_Id(Long orderId);
    Long countByProductId(Long productId);
    List<OrderItems> findByProductId(Long productId);

}
