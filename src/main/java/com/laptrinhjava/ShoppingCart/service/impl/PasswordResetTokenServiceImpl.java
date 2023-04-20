package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.PasswordResetToken;
import com.laptrinhjava.ShoppingCart.entity.Users;
import com.laptrinhjava.ShoppingCart.reponsitory.IPasswordResetTokenRepository;
import com.laptrinhjava.ShoppingCart.service.IPasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class PasswordResetTokenServiceImpl implements IPasswordResetTokenService {

    @Autowired
    private IPasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public void createPasswordResetTokenForUser(Users user, String token) {
        passwordResetTokenRepository.save(new PasswordResetToken(token, user));
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);
        if(passToken == null){
            return "invalidToken";
        }
        else {
            if(isTokenExpired(passToken)) return "expired";
            else return null;
        }
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public void deleteById(Long id) {
        passwordResetTokenRepository.deleteById(id);
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
}
