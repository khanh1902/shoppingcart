package com.laptrinhjava.ShoppingCart.service;

import com.laptrinhjava.ShoppingCart.entity.Users;
import com.laptrinhjava.ShoppingCart.payload.request.auth.ChangePasswordRequest;
import com.laptrinhjava.ShoppingCart.payload.request.auth.UserRequest;
import com.laptrinhjava.ShoppingCart.payload.response.auth.UserResponse;
import com.laptrinhjava.ShoppingCart.security.oauth2.CustomOAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IUserService {
    Users findByEmail(String email);
    Users findUsersById(Long id);
    Optional<Users> findEmail(String email);
    List<Users> findALlByEmail(String email);
    Boolean existsByEmail(String email);
    Users save(Users user);
    void processOAuthPostLogin(CustomOAuth2User user);
    List<Users> findByRoles_Id(Long roles_id);
    UserResponse update(UserRequest userRequest);
    List<UserResponse> getAllUser(List<Users> users);
}
