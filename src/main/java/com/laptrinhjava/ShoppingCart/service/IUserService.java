package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IUserService {
    User findByUserName(String userName);
    Optional<User> findUserName(String userName);
    List<User> findALlByUserName(String userName);
    Boolean existsByUserName(String userName);
    Boolean existsByEmail(String email);
    User save(User user);
}
