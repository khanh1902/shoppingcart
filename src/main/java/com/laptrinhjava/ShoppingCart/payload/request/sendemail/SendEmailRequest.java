package com.laptrinhjava.ShoppingCart.payload.request.sendemail;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendEmailRequest {
    private String toEmail;
    private String subject;
    private String body;
}
