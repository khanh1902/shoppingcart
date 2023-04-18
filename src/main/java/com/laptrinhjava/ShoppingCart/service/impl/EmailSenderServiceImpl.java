package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.Users;
import com.laptrinhjava.ShoppingCart.payload.request.sendemail.SendEmailRequest;
import com.laptrinhjava.ShoppingCart.service.IEmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

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

    @Override
    public SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, Users user) {

            String url = contextPath + "/api/user/changePassword?token=" + token;
//            String message = messages.getMessage("message.resetPassword",
//                    null, locale);
//            return constructEmail("Reset Password", message + " \r\n" + url, user);
        return null;
    }

    @Override
    public SimpleMailMessage constructEmail(String subject, String body, Users user) {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setSubject(subject);
            email.setText(body);
            email.setTo(user.getEmail());
            email.setFrom("vankhanh.dev@gmail.com");
            return email;
    }
}
