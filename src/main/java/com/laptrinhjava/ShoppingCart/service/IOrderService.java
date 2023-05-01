package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Order;
import com.laptrinhjava.ShoppingCart.payload.request.order.OrderRequest;
import com.laptrinhjava.ShoppingCart.payload.response.order.OrderForUserResponse;
import com.laptrinhjava.ShoppingCart.payload.response.order.OrderResponse;
import com.laptrinhjava.ShoppingCart.payload.response.order.UpdateStatusResponse;

import java.util.List;

public interface IOrderService {
    Order findOrderById(Long id);
    Order findByUsers_Id(Long userId);
    Order saveOrder(Order order);
    Long save(OrderRequest orderRequest) throws Exception;

    UpdateStatusResponse updateStatusOrder(Long orderId, String newStatus) throws Exception;

    void deleteById(Long id);

    List<OrderResponse> getAllOrderForUserByStatus(String status) throws Exception;
    List<OrderForUserResponse> getAllOrderUserForAdmin(Long userId) throws Exception;

    OrderResponse getOneOrderForUser(Long orderId) throws Exception;

    List<OrderResponse> getAllOrderForAdmin() throws Exception;
    Long totalOrderOfDay();

}
