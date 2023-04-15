package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.Order;
import com.laptrinhjava.ShoppingCart.entity.Users;
import com.laptrinhjava.ShoppingCart.payload.request.order.OrderRequest;
import com.laptrinhjava.ShoppingCart.payload.request.order.UpdateStatusRequest;
import com.laptrinhjava.ShoppingCart.payload.request.sendemail.SendEmailRequest;
import com.laptrinhjava.ShoppingCart.payload.response.order.OrderItemsResponse;
import com.laptrinhjava.ShoppingCart.payload.response.order.OrderResponse;
import com.laptrinhjava.ShoppingCart.payload.response.order.UpdateStatusResponse;
import com.laptrinhjava.ShoppingCart.reponsitory.IOrderRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.IUserRepository;
import com.laptrinhjava.ShoppingCart.service.IEmailSenderService;
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


    @Qualifier("emailSenderServiceImpl")
    @Autowired
    private IEmailSenderService emailSenderService;

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findOrderById(id);
    }

    @Override
    public Order findByUsers_Id(Long userId) {
        return orderRepository.findByUsers_Id(userId);
    }

    public Double discount(Long discountPercent, Double price) {
        if (discountPercent != null) return (Double) price - price * discountPercent / 100L;
        // neu khong co disount percent thi tra ve gia ban dau
        return price;
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
    public UpdateStatusResponse updateStatusOrder(Long orderId, String newStatus) {
        Order findOrder = orderRepository.findOrderById(orderId);
        if (newStatus.toLowerCase().equals("successfully")) {
            findOrder.setStatus(newStatus);
            orderRepository.save(findOrder);
            SendEmailRequest sendEmailRequest = new SendEmailRequest();
            sendEmailRequest.setToEmail(findOrder.getEmail());
            sendEmailRequest.setSubject("Confirm Order");

            StringBuilder body = new StringBuilder();
            body.append("Congratulations! Your order has been successful\n");
            body.append("Order ID: ").append(findOrder.getId()).append("\n");
            body.append("Total price: ").append(findOrder.getTotalPrice()).append("$\n");
            body.append("Your order will be delivered to you within 3-5 days").append("\n\n");
            body.append("Thank You");
            sendEmailRequest.setBody(body.toString());
            sendEmailRequest.setBody(sendEmailRequest.getBody());
            emailSenderService.sendEmail(sendEmailRequest);
            return new UpdateStatusResponse(findOrder.getId(), findOrder.getStatus());

        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}
