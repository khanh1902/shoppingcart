package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.entity.Order;
import com.laptrinhjava.ShoppingCart.entity.OrderItems;
import com.laptrinhjava.ShoppingCart.entity.Products;
import com.laptrinhjava.ShoppingCart.entity.Users;
import com.laptrinhjava.ShoppingCart.payload.ResponseObject;
import com.laptrinhjava.ShoppingCart.payload.response.dashboard.DashboardResponse;
import com.laptrinhjava.ShoppingCart.payload.response.dashboard.SalesMonthResponse;
import com.laptrinhjava.ShoppingCart.payload.response.dashboard.Top5Products;
import com.laptrinhjava.ShoppingCart.payload.response.dashboard.Top5Users;
import com.laptrinhjava.ShoppingCart.payload.response.order.CountOrderForUserResponse;
import com.laptrinhjava.ShoppingCart.payload.response.product.CountProductResponse;
import com.laptrinhjava.ShoppingCart.service.IOrderItemsService;
import com.laptrinhjava.ShoppingCart.service.IOrderService;
import com.laptrinhjava.ShoppingCart.service.IUserService;
import com.laptrinhjava.ShoppingCart.service.productService.IProductService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private IOrderService orderService;

    @Qualifier("orderItemsServiceImpl")
    @Autowired
    private IOrderItemsService orderItemsService;

    @Qualifier("userServiceImpl")
    @Autowired
    private IUserService userService;

    @Qualifier("productsServiceImpl")
    @Autowired
    private IProductService productService;

    @GetMapping("/total-order")
    public ResponseEntity<ResponseObject> getTotalOrderOfDay() {

        Long totalOrder = 0L;
        Long totalProduct = 0L;
        Double totalPrice = 0D;
        Long orderReceived = 0L;
        Long orderPending = 0L;
        Long orderDelivering = 0L;
        Long orderSuccess = 0L;
        List<Order> orders = orderService.findAll();
        for (Order order : orders) {
            totalOrder++;
            if (order.getStatus().equalsIgnoreCase("pending")) orderPending++;
            else if (order.getStatus().equalsIgnoreCase("received")) {
                orderReceived++;
                totalPrice += order.getTotalPrice();
                List<OrderItems> findOrderItem = orderItemsService.findByOrder_Id(order.getId());
                totalProduct += (long) findOrderItem.size();
            } else if (order.getStatus().equalsIgnoreCase("delivering")) {
                orderDelivering++;
                totalPrice += order.getTotalPrice();
                List<OrderItems> findOrderItem = orderItemsService.findByOrder_Id(order.getId());
                totalProduct += (long) findOrderItem.size();
            } else if (order.getStatus().equalsIgnoreCase("success")) {
                orderSuccess++;
                totalPrice += order.getTotalPrice();
                List<OrderItems> findOrderItem = orderItemsService.findByOrder_Id(order.getId());
                totalProduct += (long) findOrderItem.size();
            }
        }

        // Top 5 user order nhieu nhat
        List<Users> findAllUser = userService.findAll();
        List<Top5Users> top5User = new ArrayList<>();
        List<CountOrderForUserResponse> countOrders = new ArrayList<>();
        for (Users user : findAllUser) {
            Long countOrder = orderService.countByUsers_Id(user.getId());
            if (countOrder != 0) {
                CountOrderForUserResponse countOrderForUserResponse = new CountOrderForUserResponse(user.getId(), countOrder);
                countOrders.add(countOrderForUserResponse);
            }
        }

        countOrders.sort((c1, c2) -> c2.getCountOrder().compareTo(c1.getCountOrder())); // sort danh sách giảm dần theo countOrder;
        if (countOrders.size() < 5) {
            for (CountOrderForUserResponse countOrder : countOrders) {
                Users findUser = userService.findUsersById(countOrder.getUserId());
                List<Order> findAllOrderByUserId = orderService.findAllByUsers_Id(findUser.getId());
                Double totalPriceOrder = 0D;

                for (Order order : findAllOrderByUserId) {
                    totalPriceOrder += order.getTotalPrice();
                }
                Top5Users topUser = new Top5Users(findUser.getId(), findUser.getFullName(), findUser.getEmail(),
                        countOrder.getCountOrder(), totalPrice);
                top5User.add(topUser);
            }
        } else {
            for (int i = 0; i < 5; i++) {
                Users findUser = userService.findUsersById(countOrders.get(i).getUserId());
                List<Order> findAllOrderByUserId = orderService.findAllByUsers_Id(findUser.getId());
                Double totalPriceOrder = 0D;

                for (Order order : findAllOrderByUserId) {
                    totalPriceOrder += order.getTotalPrice();
                }
                Top5Users topUser = new Top5Users(findUser.getId(), findUser.getFullName(), findUser.getEmail(),
                        countOrders.get(i).getCountOrder(), totalPrice);
                top5User.add(topUser);
            }
        }


        // Top 5 sản phẩm bán chạy nhất
        List<Products> findAllProducts = productService.findAll();
        List<Top5Products> top5Products = new ArrayList<>();
        List<CountProductResponse> countProductResponses = new ArrayList<>();
        for (Products product : findAllProducts) {
            List<OrderItems> findAllOrderItemByProductId = orderItemsService.findByProductId(product.getId());
            Long countProduct = 0L;
            for (OrderItems orderItem : findAllOrderItemByProductId) {
                countProduct += orderItem.getQuantity();
            }
            if (countProduct != 0L) {
                CountProductResponse countProductResponse = new CountProductResponse(product.getId(), countProduct);
                countProductResponses.add(countProductResponse);
            }
        }

        // sort giam dan theo countProduct
        countProductResponses.sort((c1, c2) -> c2.getCountProduct().compareTo(c1.getCountProduct()));
        if (countProductResponses.size() < 5) {
            for (CountProductResponse countProduct : countProductResponses) {
                Products findProduct = productService.findProductById(countProduct.getProductId());
                Top5Products top5Product = new Top5Products(findProduct.getId(), findProduct.getImageUrl(), findProduct.getName(), countProduct.getCountProduct());
                top5Products.add(top5Product);
            }
        } else {
            for (int i = 0; i < 5; i++) {
                Products findProduct = productService.findProductById(countProductResponses.get(i).getProductId());
                Top5Products top5Product = new Top5Products(findProduct.getId(), findProduct.getImageUrl(), findProduct.getName(), countProductResponses.get(i).getCountProduct());
                top5Products.add(top5Product);
            }
        }

        DashboardResponse dashboard = new DashboardResponse(totalOrder, totalProduct, totalPrice, orderReceived, orderPending,
                orderDelivering, orderSuccess, top5Products, top5User);


        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successfully!", dashboard)
        );
    }

    @GetMapping("/sales-month")
    public ResponseEntity<ResponseObject> salesMonth(@RequestParam(name = "year") Integer year) {
        List<SalesMonthResponse> salesMonthResponses = new ArrayList<>();
        for (int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date startOfMonth = cal.getTime();

            int lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            Date endOfMonth = cal.getTime();
            List<Order> orders = orderService.findAll();
            Double totalPrice = 0D;
            Long totalProduct = 0L;
            for (Order order : orders) {
                if (order.getCreatedDate().after(startOfMonth) && order.getCreatedDate().before(endOfMonth)) {
                    if (!order.getStatus().equalsIgnoreCase("pending") &&
                            !order.getStatus().equalsIgnoreCase("cancel")) {
                        totalPrice += order.getTotalPrice();
                        List<OrderItems> findOrderItem = orderItemsService.findByOrder_Id(order.getId());
                        totalProduct += (long) findOrderItem.size();

                    }

                }
            }
            SalesMonthResponse salesMonthResponse = new SalesMonthResponse(month + 1, totalPrice, totalProduct);
            salesMonthResponses.add(salesMonthResponse);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successfully!", salesMonthResponses)
        );
    }
}
