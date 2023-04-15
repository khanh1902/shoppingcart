package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Order;
import com.laptrinhjava.ShoppingCart.payload.request.order.OrderRequest;
import com.laptrinhjava.ShoppingCart.payload.request.order.UpdateStatusRequest;
import com.laptrinhjava.ShoppingCart.payload.response.order.OrderResponse;
import com.laptrinhjava.ShoppingCart.payload.response.order.UpdateStatusResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface IOrderService {
    Order findOrderById(Long id);
    Order findByUsers_Id(Long userId);
    Long save(OrderRequest orderRequest);

    UpdateStatusResponse updateStatusOrder(Long orderId, String newStatus);

    void deleteById(Long id);

    List<OrderResponse> getAllOrderForUser(String sortBy, String status);

    OrderResponse getOneOrderForUser(Long orderId);

    List<OrderResponse> getAllOrderForAdmin(String sortBy, String status);

}
