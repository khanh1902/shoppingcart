package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.Order;
import com.laptrinhjava.ShoppingCart.entity.Users;
import com.laptrinhjava.ShoppingCart.payload.request.order.OrderRequest;
import com.laptrinhjava.ShoppingCart.payload.response.order.OrderItemsResponse;
import com.laptrinhjava.ShoppingCart.payload.response.order.OrderResponse;
import com.laptrinhjava.ShoppingCart.reponsitory.IOrderRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.IUserRepository;
import com.laptrinhjava.ShoppingCart.service.IOrderService;
import com.laptrinhjava.ShoppingCart.service.productService.IOrderItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.laptrinhjava.ShoppingCart.common.HandleAuth.getUsername;


@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IUserRepository userRepository;

    @Qualifier("orderItemsServiceImpl")
    @Autowired
    private IOrderItemsService orderItemsService;

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findOrderById(id);
    }

    @Override
    public Order findByUsers_Id(Long userId) {
        return orderRepository.findByUsers_Id(userId);
    }

    @Override
    public Long save(OrderRequest orderRequest) {
        String email = getUsername();
        Users findUser = userRepository.findByEmail(email);
        String status = "Pending";
        Double totalPrice = 0D;
        Order order = new Order(findUser, orderRequest.getFullName(), orderRequest.getEmail(), null, orderRequest.getAddress(),
                orderRequest.getPhoneNumber(), status);
        orderRepository.save(order);

        // luu danh sach san pham
        List<OrderItemsResponse> orderItemsResponses = orderItemsService.addProductsToOrder(order, orderRequest.getCartItemIds());
        for (OrderItemsResponse orderItemsResponse : orderItemsResponses) {
            totalPrice += orderItemsResponse.getPrice();
        }
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        return order.getId();
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}
