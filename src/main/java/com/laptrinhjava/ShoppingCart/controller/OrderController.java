package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.payload.request.order.OrderRequest;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @PostMapping
    public ResponseEntity<ResponseObject> save(@RequestBody OrderRequest orderRequest){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Your order is being confirmed!", orderService.save(orderRequest))
        );
    }

    @PutMapping
    public ResponseEntity<ResponseObject> updateStatusOrder(@RequestParam(name = "orderId") Long orderId,
                                                            @RequestParam(name = "status") String newStatus){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update successfully!", orderService.updateStatusOrder(orderId, newStatus))
        );
    }

}
