package com.laptrinhjava.ShoppingCart.payload.response.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String sex;
    private String dateOfBirth;
    private String email;
    private List<String> roles;

    // constructor
    public JwtResponse(String token, Long id, String fullName, String email, String phoneNumber,
            String sex, String dateOfBirth, List<String> roles) {
        this.token = token;
        this.type = type;
        this.email = email;
        this.fullName = fullName;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.roles = roles;
    }
}
