package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.User;
import com.laptrinhjava.ShoppingCart.security.oauth2.CustomOAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IUserService {
    User findByEmail(String email);
    Optional<User> findEmail(String email);
    List<User> findALlByEmail(String email);
    Boolean existsByEmail(String email);
    User save(User user);
    void processOAuthPostLogin(CustomOAuth2User user);
}
