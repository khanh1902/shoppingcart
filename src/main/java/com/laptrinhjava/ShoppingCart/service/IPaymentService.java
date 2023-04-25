package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.payload.response.payment.PaymentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPaymentService {
    List<PaymentResponse> findAll();
}
