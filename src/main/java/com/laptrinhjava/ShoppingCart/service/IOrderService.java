package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Order;
import com.laptrinhjava.ShoppingCart.payload.request.order.OrderRequest;
import com.laptrinhjava.ShoppingCart.payload.request.order.UpdateStatusRequest;
import com.laptrinhjava.ShoppingCart.payload.response.order.UpdateStatusResponse;

public interface IOrderService {
    Order findOrderById(Long id);
    Order findByUsers_Id(Long userId);
    Long save(OrderRequest orderRequest);

    UpdateStatusResponse updateStatusOrder(Long orderId, String newStatus);

    void deleteById(Long id);
}
