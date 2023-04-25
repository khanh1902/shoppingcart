package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.Payment;
import com.laptrinhjava.ShoppingCart.payload.response.payment.PaymentResponse;
import com.laptrinhjava.ShoppingCart.reponsitory.IPaymentRepository;
import com.laptrinhjava.ShoppingCart.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private IPaymentRepository paymentRepository;

    @Override
    public List<PaymentResponse> findAll() {
        List<PaymentResponse> paymentResponseList = new ArrayList<>();
        List<Payment> findAllPayment = paymentRepository.findAll();
        for (Payment payment : findAllPayment){
            PaymentResponse paymentResponse = new PaymentResponse(payment.getId(), payment.getCode(), payment.getName());
            paymentResponseList.add(paymentResponse);
        }
        return paymentResponseList;
    }
}
