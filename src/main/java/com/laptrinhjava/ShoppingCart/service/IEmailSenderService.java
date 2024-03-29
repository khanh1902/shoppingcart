package com.laptrinhjava.ShoppingCart.service;

import com.amazonaws.services.identitymanagement.model.User;
import com.laptrinhjava.ShoppingCart.entity.Users;
import com.laptrinhjava.ShoppingCart.payload.request.sendemail.SendEmailRequest;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Service
public interface IEmailSenderService {
    void sendEmail(SendEmailRequest sendEmailRequest) throws MailException;
    void constructResetTokenEmail(String contextPath, Locale locale, String token, Users user) throws MessagingException;
}
