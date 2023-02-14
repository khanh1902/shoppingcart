package com.laptrinhjava.ShoppingCart.payload.response;

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
    private String email;
    private List<String> roles;

    // constructor
    public JwtResponse(String token, Long id, String fullName, String email, List<String> roles) {
        this.token = token;
        this.type = type;
        this.email = email;
        this.fullName = fullName;
        this.id = id;
        this.roles = roles;
    }
}
