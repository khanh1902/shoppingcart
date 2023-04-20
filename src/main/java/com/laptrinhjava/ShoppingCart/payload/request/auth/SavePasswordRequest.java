package com.laptrinhjava.ShoppingCart.payload.request.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SavePasswordRequest {
    String password;
    String confirmPassword;
}
