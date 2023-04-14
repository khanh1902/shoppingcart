package com.laptrinhjava.ShoppingCart.service.productService;

import com.laptrinhjava.ShoppingCart.entity.Order;
import com.laptrinhjava.ShoppingCart.payload.response.order.OrderItemsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderItemsService {
    List<OrderItemsResponse> addProductsToOrder(Order order, List<Long> cartItemId);
}
