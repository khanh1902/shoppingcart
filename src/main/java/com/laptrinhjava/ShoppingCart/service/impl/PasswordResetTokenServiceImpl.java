package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.PasswordResetToken;
import com.laptrinhjava.ShoppingCart.entity.Users;
import com.laptrinhjava.ShoppingCart.reponsitory.IPasswordResetTokenRepository;
import com.laptrinhjava.ShoppingCart.service.IPasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetTokenServiceImpl implements IPasswordResetTokenService {

    @Autowired
    private IPasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public void createPasswordResetTokenForUser(Users user, String token) {
        passwordResetTokenRepository.save(new PasswordResetToken(token, user));
    }
}
