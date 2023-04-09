package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.EProvider;
import com.laptrinhjava.ShoppingCart.entity.Users;
import com.laptrinhjava.ShoppingCart.reponsitory.IUserRepository;
import com.laptrinhjava.ShoppingCart.security.oauth2.CustomOAuth2User;
import com.laptrinhjava.ShoppingCart.service.IRoleService;
import com.laptrinhjava.ShoppingCart.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleService roleService;

//    @Autowired
//    private ICartService cartService;


    @Override
    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<Users> findEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    @Override
    public List<Users> findALlByEmail(String email) {
        return userRepository.findAllByEmail(email);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Users save(Users user) {
        return userRepository.save(user);
    }

    @Override
    public void processOAuthPostLogin(CustomOAuth2User user) {
        Users existUser = userRepository.findByEmail(user.getEmail());

        if (existUser == null) {
            Users newUser = new Users();
            newUser.setEmail(user.getEmail());
            newUser.setProvider(EProvider.GOOGLE);
            newUser.setFullName(user.getName());

            //set role for gg api
            userRepository.save(newUser);
//            cartService.save(new Cart(newUser.getId(), newUser.getId(), null));

        }
    }

    @Override
    public List<Users> findByRoles_Id(Long roles_id) {
        return userRepository.findByRoles_Id(roles_id);
    }
}
