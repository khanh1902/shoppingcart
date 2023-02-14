package com.laptrinhjava.ShoppingCart.payload.response;

import com.laptrinhjava.ShoppingCart.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;

    private Set<Role> roles;

    public UserResponse(Long id, String fullName, String email, Set<Role> roles) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
    }
}
