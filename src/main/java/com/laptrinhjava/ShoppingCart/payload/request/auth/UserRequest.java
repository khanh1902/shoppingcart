package com.laptrinhjava.ShoppingCart.payload.request.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequest {
    private String fullName;
    private String phoneNumber;
    private String sex;
    private String dateOfBirth;
}
