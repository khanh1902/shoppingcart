package com.laptrinhjava.ShoppingCart.payload.response.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthResponse {
    private Long id;
    private String email;
    private String fullName;
    private List<String> roles;

    public AuthResponse(Long id, String fullName, String email, List<String> roles) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
    }
}
