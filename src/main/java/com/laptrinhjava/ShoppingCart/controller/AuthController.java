package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.payload.request.SigninRequest;
import com.laptrinhjava.ShoppingCart.payload.request.SignupRequest;
import com.laptrinhjava.ShoppingCart.payload.response.JwtResponse;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.entity.ERole;
import com.laptrinhjava.ShoppingCart.entity.Role;
import com.laptrinhjava.ShoppingCart.entity.User;
import com.laptrinhjava.ShoppingCart.security.jwt.JwtUtils;
import com.laptrinhjava.ShoppingCart.security.service.UserDetailsImpl;
import com.laptrinhjava.ShoppingCart.service.IRoleService;
import com.laptrinhjava.ShoppingCart.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth")
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

    // đăng nhập
    @PostMapping("/signin")
    public ResponseEntity<ResponseObject> authenticateUser(@Valid @RequestBody SigninRequest signinRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getUserName(), signinRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);

        User user = userService.findByUserName(signinRequest.getUserName()); // get fullName

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Login successfully!", new JwtResponse(jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        user.getFullName(),
                        user.getEmail(),
                        roles))
        );
    }

    // signup: dang ky
    @PostMapping("/signup")
    public ResponseEntity<ResponseObject> signup(@Valid @RequestBody SignupRequest signupRequest) {
        // kiểm tra trùng tên đăng nhập
        if (userService.existsByUserName(signupRequest.getUserName())) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Username is already taken!", "")
            );
        }

        // kiểm tra trùng email
        if (userService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Email is already taken!", "")
            );
        }

        User user = new User(signupRequest.getUserName(),
                encoder.encode(signupRequest.getPassword()),
                signupRequest.getFullName(),
                signupRequest.getEmail());

        Set<String> rolesRequest = signupRequest.getRoles(); // role truyền vào từ client

        Set<Role> roles = new HashSet<>(); // role để phân quyền

        if (rolesRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Role is not found!", "")
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

        user.setRoles(roles);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "User registered successfully!", userService.save(user))
        );
    }

}
