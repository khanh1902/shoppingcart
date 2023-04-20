package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.PasswordResetToken;
import com.laptrinhjava.ShoppingCart.entity.Users;
import org.springframework.stereotype.Service;

@Service
public interface IPasswordResetTokenService {
    void createPasswordResetTokenForUser(Users user, String token);
    String validatePasswordResetToken(String token);
    PasswordResetToken findByToken(String token);
    void deleteById(Long id);

}
