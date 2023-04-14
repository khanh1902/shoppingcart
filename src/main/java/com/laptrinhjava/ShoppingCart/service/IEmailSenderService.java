package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.payload.request.sendemail.SendEmailRequest;
import org.springframework.stereotype.Service;

@Service
public interface IEmailSenderService {
    void sendEmail(SendEmailRequest sendEmailRequest);
}
