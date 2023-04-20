package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.ERole;
import com.laptrinhjava.ShoppingCart.entity.Role;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IRoleService {
    Optional<Role> findByName(ERole name);
    Role findRoleById(Long id);
}
