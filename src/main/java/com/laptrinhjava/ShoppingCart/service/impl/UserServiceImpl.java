package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.User;
import com.laptrinhjava.ShoppingCart.reponsitory.UserRepository;
import com.laptrinhjava.ShoppingCart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public Optional<User> findUserName(String userName) {
        return Optional.ofNullable(userRepository.findByUserName(userName));
    }

    @Override
    public List<User> findALlByUserName(String userName) {
        return userRepository.findAllByUserName(userName);
    }

    @Override
    public Boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
