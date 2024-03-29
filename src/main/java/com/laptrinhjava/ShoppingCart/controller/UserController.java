package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.entity.ERole;
import com.laptrinhjava.ShoppingCart.entity.PasswordResetToken;
import com.laptrinhjava.ShoppingCart.entity.Role;
import com.laptrinhjava.ShoppingCart.entity.Users;
import com.laptrinhjava.ShoppingCart.payload.request.auth.ChangePasswordRequest;
import com.laptrinhjava.ShoppingCart.payload.request.auth.SavePasswordRequest;
import com.laptrinhjava.ShoppingCart.payload.request.auth.UserRequest;
import com.laptrinhjava.ShoppingCart.payload.ResponseObject;
import com.laptrinhjava.ShoppingCart.payload.response.auth.AuthResponse;
import com.laptrinhjava.ShoppingCart.payload.response.auth.UserResponse;
import com.laptrinhjava.ShoppingCart.payload.response.order.OrderResponse;
import com.laptrinhjava.ShoppingCart.security.jwt.JwtUtils;
import com.laptrinhjava.ShoppingCart.security.service.UserDetailsServiceImpl;
import com.laptrinhjava.ShoppingCart.service.IEmailSenderService;
import com.laptrinhjava.ShoppingCart.service.IPasswordResetTokenService;
import com.laptrinhjava.ShoppingCart.service.IRoleService;
import com.laptrinhjava.ShoppingCart.service.IUserService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.laptrinhjava.ShoppingCart.common.HandleAuth.getUsername;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Qualifier("userServiceImpl")
    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Qualifier("passwordResetTokenServiceImpl")
    @Autowired
    private IPasswordResetTokenService passwordResetTokenService;

    @Qualifier("emailSenderServiceImpl")
    @Autowired
    private IEmailSenderService emailSenderService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder encoder;

    @Qualifier("roleServiceImpl")
    @Autowired
    private IRoleService roleService;

    @GetMapping("/get-me")
    public ResponseEntity<ResponseObject> getInformation(@RequestHeader(name = "Authorization") String jwt) {
        try {

//        String jwt = parseJwt(request); // Lấy jwt từ request
            jwt = jwt.substring(7);
            // Lấy email từ chuỗi jwt
            String email = Jwts.parser().setSigningKey("khanhSecret").parseClaimsJws(jwt).getBody().getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            Users user = userService.findByEmail(userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Successfully!", new AuthResponse(
                            user.getId(),
                            user.getFullName(),
                            user.getEmail(),
                            roles))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", "Error!", e.getMessage()));
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    // get all user
    @GetMapping("/get-all")
    public ResponseEntity<ResponseObject> getAllUser(@RequestParam(required = false, name = "offset", defaultValue = "0") Integer offset,
                                                     @RequestParam(required = false, name = "limit", defaultValue = "10") Integer limit) {
        try {
            Optional<Role> role = roleService.findByName(ERole.ROLE_USER);
            if(role.isEmpty()) throw new Exception("Role dose not exists!");
            List<Users> findUsers = userService.findByRoles_Id(role.get().getId());
            if (findUsers.isEmpty()) throw new Exception("List user is empty!");

            List<UserResponse> userResponses = new ArrayList<>();
            for(Users user : findUsers) {
                UserResponse userResponse = new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getPhoneNumber(),
                        user.getSex(), user.getDateOfBirth());
                userResponses.add(userResponse);
            }

            PageRequest pageRequest = PageRequest.of(offset, limit);
            int start = (int) pageRequest.getOffset();
            int end = Math.min(start + pageRequest.getPageSize(), userResponses.size());
            Page<UserResponse> usersResponsePage = new PageImpl<>(userResponses.subList(start, end), pageRequest, userResponses.size());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Successfully!", usersResponsePage)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED", e.getMessage(), null)
            );
        }
    }

    /**
     * Update Information User
     **/
    @PutMapping
    public ResponseEntity<ResponseObject> updateUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Successfully!", userService.update(userRequest))
        );
    }

    /**
     * Send Email Forgot password
     **/
    @PostMapping("/resetPassword")
    public ResponseEntity<ResponseObject> resetPassword(HttpServletRequest request,
                                                        @RequestParam(name = "email") String email) {
        try {


            Users findUser = userService.findByEmail(email);
            if (findUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("FAILED", "Email does not exists!", null)
                );
            } else {
                String token = UUID.randomUUID().toString();
                passwordResetTokenService.createPasswordResetTokenForUser(findUser, token);
                emailSenderService.constructResetTokenEmail(request.getContextPath(),
                        request.getLocale(), token, findUser);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Send Successfully!", request.getLocale())
                );
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", e.getMessage(), null)
            );
        }
    }

    /**
     * Save New Password After Enter New Password
     **/
    @PostMapping("/savePassword")
    public ResponseEntity<ResponseObject> savePassword(@RequestParam(name = "token") String token,
                                                       @RequestBody SavePasswordRequest savePasswordRequest) {
        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);
        if (passwordResetToken != null) {
            String checkToken = passwordResetTokenService.validatePasswordResetToken(passwordResetToken.getToken());
            if (checkToken != null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("FAILED", "Error!", checkToken)
                );
            }
            Users findUser = userService.findUsersById(passwordResetToken.getUser().getId());
            if (!savePasswordRequest.getPassword().equals(savePasswordRequest.getConfirmPassword())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("FAILED", "Password confirm not match!", null)
                );
            }
            findUser.setPassword(encoder.encode(savePasswordRequest.getPassword()));
            userService.save(findUser);
            passwordResetTokenService.deleteById(passwordResetToken.getId());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Update new password successfully!", null)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED", "Token does not exists!", null)
            );
        }
    }


    /**
     * Change Password For User
     **/
    @PutMapping("/changePassword")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseObject> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            String email = getUsername();
        Users findUser = userService.findByEmail(email);
        if(!encoder.matches(changePasswordRequest.getOldPassword(), findUser.getPassword())) throw new Exception("Old Password does not match!");
        if(!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) throw new Exception("Confirm password does not match!");
        if(changePasswordRequest.getOldPassword().equals(changePasswordRequest.getNewPassword())) throw new Exception("The new password must be different from the old password!");
        findUser.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
        userService.save(findUser);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Change password successfully!", null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", e.getMessage(), null)
            );
        }
    }
}
