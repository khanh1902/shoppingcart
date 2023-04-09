package com.laptrinhjava.ShoppingCart.payload.request.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    @NotBlank
    private String fullName;

    private Set<String> roles;
}
