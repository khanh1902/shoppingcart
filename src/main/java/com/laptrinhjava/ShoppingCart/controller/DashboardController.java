package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.payload.ResponseObject;
import com.laptrinhjava.ShoppingCart.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private IOrderService orderService;

    @GetMapping("/total-order")
    public ResponseEntity<ResponseObject> getTotalOrderOfDay(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successfully!", orderService.totalOrderOfDay())
        );
    }
}
