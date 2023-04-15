package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.*;
import com.laptrinhjava.ShoppingCart.payload.request.order.OrderRequest;
import com.laptrinhjava.ShoppingCart.payload.request.order.UpdateStatusRequest;
import com.laptrinhjava.ShoppingCart.payload.request.sendemail.SendEmailRequest;
import com.laptrinhjava.ShoppingCart.payload.response.order.OrderItemsResponse;
import com.laptrinhjava.ShoppingCart.payload.response.order.OrderResponse;
import com.laptrinhjava.ShoppingCart.payload.response.order.UpdateStatusResponse;
import com.laptrinhjava.ShoppingCart.reponsitory.IOrderItemsRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.IOrderRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.IUserRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.IOptionValuesRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.IOptionsRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.IProductRepository;
import com.laptrinhjava.ShoppingCart.reponsitory.productRepository.IVariantValuesRepository;
import com.laptrinhjava.ShoppingCart.service.IEmailSenderService;
import com.laptrinhjava.ShoppingCart.service.IOrderService;
import com.laptrinhjava.ShoppingCart.service.productService.IOrderItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.laptrinhjava.ShoppingCart.common.HandleAuth.getUsername;
import static com.laptrinhjava.ShoppingCart.common.HandleChar.upperFirstString;


@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IUserRepository userRepository;

    @Qualifier("orderItemsServiceImpl")
    @Autowired
    private IOrderItemsService orderItemsService;

    @Autowired
    private IOrderItemsRepository orderItemsRepository;


    @Qualifier("emailSenderServiceImpl")
    @Autowired
    private IEmailSenderService emailSenderService;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IVariantValuesRepository variantValuesRepository;

    @Autowired
    private IOptionsRepository optionsRepository;

    @Autowired
    private IOptionValuesRepository optionValuesRepository;

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
        String status = "pending";
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
        if (newStatus.equalsIgnoreCase("successfully")) {
            findOrder.setStatus(newStatus.toLowerCase());
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

    @Override
    public List<OrderResponse> getAllOrderForUser(String sortBy, String status) {
        String email = getUsername();
        Users findUser = userRepository.findByEmail(email);
        Sort sort = Sort.by(sortBy).ascending();
        List<Order> orders = null;
        if (status != null) {
            orders = orderRepository.findAllByUsers_IdAndStatusContainingIgnoreCase(findUser.getId(), status.toLowerCase(), sort);
        } else {
            orders = orderRepository.findAllByUsers_Id(findUser.getId(), sort);
        }

        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setOrderId(order.getId());
            orderResponse.setEmail(order.getEmail());
            orderResponse.setAddress(order.getAddress());
            orderResponse.setTotalPrice(order.getTotalPrice());
            orderResponse.setFullName(order.getFullName());
            orderResponse.setPhoneNumber(order.getPhoneNumber());
            orderResponse.setStatus(order.getStatus());
            orderResponse.setUserId(order.getId());
            orderResponse.setOrderItemsResponses(convertOrderItemToOrderItemResponse(orderItemsRepository.findByOrder_Id(order.getId())));
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    @Override
    public OrderResponse getOneOrderForUser(Long orderId) {
        String email = getUsername();
        Users findUser = userRepository.findByEmail(email);
        Order findOrder = orderRepository.findOrderById(orderId);
        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setOrderId(findOrder.getId());
        orderResponse.setEmail(findOrder.getEmail());
        orderResponse.setAddress(findOrder.getAddress());
        orderResponse.setTotalPrice(findOrder.getTotalPrice());
        orderResponse.setFullName(findOrder.getFullName());
        orderResponse.setPhoneNumber(findOrder.getPhoneNumber());
        orderResponse.setStatus(findOrder.getStatus());
        orderResponse.setUserId(findUser.getId());
        orderResponse.setOrderItemsResponses(convertOrderItemToOrderItemResponse(orderItemsRepository.findByOrder_Id(findUser.getId())));

        return orderResponse;
    }

    @Override
    public List<OrderResponse> getAllOrderForAdmin(String sortBy, String status) {
        Sort sort = Sort.by(sortBy).ascending();
        List<Order> orders = null;
        if (status != null) {
            orders = orderRepository.findALlByStatusContainingIgnoreCase(status.toLowerCase(), sort);
        } else {
            orders = orderRepository.findAll(sort);
        }
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setOrderId(order.getId());
            orderResponse.setEmail(order.getEmail());
            orderResponse.setAddress(order.getAddress());
            orderResponse.setTotalPrice(order.getTotalPrice());
            orderResponse.setFullName(order.getFullName());
            orderResponse.setPhoneNumber(order.getPhoneNumber());
            orderResponse.setStatus(order.getStatus());
            orderResponse.setUserId(order.getId());
            orderResponse.setOrderItemsResponses(convertOrderItemToOrderItemResponse(orderItemsRepository.findByOrder_Id(order.getId())));
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    public List<OrderItemsResponse> convertOrderItemToOrderItemResponse(List<OrderItems> orderItems) {
        List<OrderItemsResponse> orderItemsResponses = new ArrayList<>();
        for (OrderItems orderItem : orderItems) {
            OrderItemsResponse orderItemsResponse = new OrderItemsResponse();
            Products findProduct = productRepository.findProductById(orderItem.getProductId());
            orderItemsResponse.setOrderItemId(orderItem.getId());
            orderItemsResponse.setProductId(orderItem.getProductId());
            orderItemsResponse.setPrice(orderItem.getPrice());
            orderItemsResponse.setQuantity(orderItem.getQuantity());
            orderItemsResponse.setImageUrl(findProduct.getImageUrl());
            orderItemsResponse.setProductName(findProduct.getName());
            if (orderItem.getProductVariants() == null) {
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
        return orderItemsResponses;
    }
}
