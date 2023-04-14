package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Order;
import com.laptrinhjava.ShoppingCart.payload.request.order.OrderRequest;
import com.laptrinhjava.ShoppingCart.payload.response.order.OrderResponse;
import org.springframework.stereotype.Service;

public interface IOrderService {
    Order findOrderById(Long id);
    Order findByUsers_Id(Long userId);
    Long save(OrderRequest orderRequest);



    void deleteById(Long id);
}
