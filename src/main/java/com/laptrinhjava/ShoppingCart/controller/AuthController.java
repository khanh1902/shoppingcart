package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.entity.*;
import com.laptrinhjava.ShoppingCart.payload.request.auth.SigninRequest;
import com.laptrinhjava.ShoppingCart.payload.request.auth.SignupRequest;
import com.laptrinhjava.ShoppingCart.payload.response.auth.JwtResponse;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.security.jwt.JwtUtils;
import com.laptrinhjava.ShoppingCart.security.service.UserDetailsImpl;
import com.laptrinhjava.ShoppingCart.service.ICartService;
import com.laptrinhjava.ShoppingCart.service.IRoleService;
import com.laptrinhjava.ShoppingCart.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Qualifier("userServiceImpl")
    @Autowired
    private IUserService userService;

    @Qualifier("roleServiceImpl")
    @Autowired
    private IRoleService roleService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Qualifier("cartServiceImpl")
    @Autowired
    private ICartService cartService;

    // đăng nhập
    @PostMapping("/signin")
    public ResponseEntity<ResponseObject> authenticateUser(@Valid @RequestBody SigninRequest loginRequest, HttpServletResponse response) {

        Users findUser = userService.findByEmail(loginRequest.getEmail());
        if (findUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Email not exists!", null
                    ));
        } else if (!encoder.matches(loginRequest.getPassword(), findUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Password not match!", null)
            );
        } else {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateToken(authentication);

            Users user = userService.findByEmail(loginRequest.getEmail());

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            ResponseCookie jwtCookie = jwtUtils.generateJWTCookie(userDetails);
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

//            ResponseCookie resCookie = ResponseCookie.from("auth_token", jwt)
//                    .httpOnly(true)
//                    .sameSite("None")
//                    .secure(true)
//                    .path("/")
//                    .maxAge(24 * 60 * 60)
//                    .build();
            response.addHeader("Set-Cookie", jwtCookie.toString());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Login successfully!", new JwtResponse(jwt,
                            userDetails.getId(),
                            user.getFullName(),
                            userDetails.getUsername(),
                            user.getPhoneNumber(),
                            user.getSex(),
                            user.getDateOfBirth(),
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

        Users user = new Users(encoder.encode(signupRequest.getPassword()),
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
        cartService.save(new Cart(user.getId(), user.getId()));

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "User registered successfully!", " ")
        );
    }
    @PostMapping("/signout")
    public ResponseEntity<ResponseObject> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "You've been signed out!", null)
        );
    }

}
