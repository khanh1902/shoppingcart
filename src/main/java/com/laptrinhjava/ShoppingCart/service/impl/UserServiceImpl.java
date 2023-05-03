package com.laptrinhjava.ShoppingCart.service.impl;

import com.laptrinhjava.ShoppingCart.entity.Users;
import com.laptrinhjava.ShoppingCart.payload.request.auth.UserRequest;
import com.laptrinhjava.ShoppingCart.payload.response.auth.UserResponse;
import com.laptrinhjava.ShoppingCart.reponsitory.IUserRepository;
import com.laptrinhjava.ShoppingCart.service.IRoleService;
import com.laptrinhjava.ShoppingCart.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.laptrinhjava.ShoppingCart.common.HandleAuth.getUsername;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Qualifier("roleServiceImpl")
    @Autowired
    private IRoleService roleService;

    @Override
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users findUsersById(Long id) {
        return userRepository.findUsersById(id);
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
    public List<Users> findByRoles_Id(Long roles_id) {
        return userRepository.findByRoles_Id(roles_id);
    }

    @Override
    public UserResponse update(UserRequest userRequest) {
        String email = getUsername();
        Users findUser = userRepository.findByEmail(email);
        if(userRequest.getFullName() != null) findUser.setFullName(userRequest.getFullName());
        if(userRequest.getPhoneNumber() != null) findUser.setPhoneNumber(userRequest.getPhoneNumber());
        if (userRequest.getSex() != null) findUser.setSex(userRequest.getSex());
        if (userRequest.getDateOfBirth() != null) findUser.setDateOfBirth(userRequest.getDateOfBirth());
        userRepository.save(findUser);
        return new UserResponse(findUser.getId(), findUser.getEmail(),findUser.getFullName(), findUser.getPhoneNumber(), findUser.getSex(), findUser.getDateOfBirth());
    }

    @Override
    public List<UserResponse> getAllUser(List<Users> users) {
        List<UserResponse> userResponses = new ArrayList<>();
        for(Users user : users){
            UserResponse userResponse = new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getPhoneNumber(), user.getSex(), user.getDateOfBirth());
            userResponses.add(userResponse);
        }
        return userResponses;
    }
}
