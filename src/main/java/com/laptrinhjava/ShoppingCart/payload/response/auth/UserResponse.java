package com.laptrinhjava.ShoppingCart.payload.response.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {
    private String fullName;
    private String phoneNumber;
    private String sex;
    private String dateOfBirth;

    public UserResponse(String fullName, String phoneNumber, String sex, String dateOfBirth) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
    }
}
