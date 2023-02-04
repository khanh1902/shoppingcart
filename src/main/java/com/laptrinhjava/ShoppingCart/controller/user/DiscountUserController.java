package com.laptrinhjava.ShoppingCart.controller.user;

import com.laptrinhjava.ShoppingCart.entity.Discount;
import com.laptrinhjava.ShoppingCart.service.IDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/discount")
public class DiscountUserController {
    @Autowired
    IDiscountService discountService;

    /**
     * Method: Find All Discount
     * **/
    @GetMapping("/findall")
    public List<Discount> findAll() {
        return discountService.findAll();
    }
}
