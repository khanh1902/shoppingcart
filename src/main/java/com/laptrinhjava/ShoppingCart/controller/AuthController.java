package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.entity.*;
import com.laptrinhjava.ShoppingCart.payload.request.SigninRequest;
import com.laptrinhjava.ShoppingCart.payload.request.SignupRequest;
import com.laptrinhjava.ShoppingCart.payload.response.JwtResponse;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.payload.response.UserResponse;
import com.laptrinhjava.ShoppingCart.security.jwt.JwtUtils;
import com.laptrinhjava.ShoppingCart.security.service.UserDetailsImpl;
import com.laptrinhjava.ShoppingCart.service.ICartService;
import com.laptrinhjava.ShoppingCart.service.IRoleService;
import com.laptrinhjava.ShoppingCart.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ICartService cartService;

    // đăng nhập
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninRequest loginRequest, HttpServletResponse response) {

        User findUser = userService.findByEmail(loginRequest.getEmail());
        if (findUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Email not exists!", null
                    ));
        }
        else if (!encoder.matches(loginRequest.getPassword(), findUser.getPassword())){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Password not match!", null)
            );
        }
        else {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateToken(authentication);

            User user = userService.findByEmail(loginRequest.getEmail());

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            final ResponseCookie responseCookie = ResponseCookie
                    .from("auth_token", jwt)
                    .secure(true)
                    .httpOnly(false)
                    .path("/")
                    .maxAge(60 * 60 * 24)
                    .sameSite("None")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Login successfully!", new JwtResponse(jwt,
                            userDetails.getId(),
                            userDetails.getUsername(),
                            user.getEmail(),
                            roles))
            );
        }
    }

    // signup: dang ky
    @PostMapping("/signup")
    public ResponseEntity<ResponseObject> signup(@Valid @RequestBody SignupRequest signupRequest) {

        // kiểm tra trùng email
        if (userService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Email is already taken!", null)
            );
        }

        User user = new User(encoder.encode(signupRequest.getPassword()),
                signupRequest.getFullName(),
                signupRequest.getEmail());

        Set<String> rolesRequest = signupRequest.getRoles(); // role truyền vào từ client

        Set<Role> roles = new HashSet<>(); // role để phân quyền

        if (rolesRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Role is empty!", null)
            );
        } else {
            rolesRequest.forEach(role -> {
                if (role.equals("admin")) {
                    Role adminRole = roleService.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Role is not found!"));
                    roles.add(adminRole);
                }
                if (role.equals("user")) {
                    Role userRole = roleService.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Role is not found!"));
                    roles.add(userRole);

                }

            });
        }

        user.setProvider(EProvider.LOCAL);
        user.setRoles(roles);
        userService.save(user);

        // tao gio hang cho user
        user.getRoles().forEach(role -> {
            if (role.getName().equals(ERole.ROLE_USER)) {
                cartService.save(new Cart(user.getId(), user.getId(), null));
            }
        });

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "User registered successfully!", " ")
        );
    }

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
