package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.entity.User;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.payload.response.UserResponse;
import com.laptrinhjava.ShoppingCart.security.service.UserDetailsImpl;
import com.laptrinhjava.ShoppingCart.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserService userService;

        @GetMapping("/get-me")
    public ResponseEntity<ResponseObject> getInformation(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        String email = userDetails.getUsername();
        User user = userService.findByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Login successfully!", new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getFullName(),
                        roles))
        );
    }
}
