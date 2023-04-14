package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.payload.request.sendemail.SendEmailRequest;
import com.laptrinhjava.ShoppingCart.service.IEmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements IEmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(SendEmailRequest sendEmailRequest) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("vankhanh.dev@gmail.com");
        mailMessage.setTo(sendEmailRequest.getToEmail());
        mailMessage.setSubject(sendEmailRequest.getSubject());
        mailMessage.setText(sendEmailRequest.getBody());

        mailSender.send(mailMessage);
    }
}
