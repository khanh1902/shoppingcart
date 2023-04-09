package com.laptrinhjava.ShoppingCart.controller;

import com.laptrinhjava.ShoppingCart.entity.Users;
import com.laptrinhjava.ShoppingCart.payload.response.ResponseObject;
import com.laptrinhjava.ShoppingCart.payload.response.auth.UserResponse;
import com.laptrinhjava.ShoppingCart.security.jwt.JwtUtils;
import com.laptrinhjava.ShoppingCart.security.service.UserDetailsServiceImpl;
import com.laptrinhjava.ShoppingCart.service.IUserService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/get-me")
    public ResponseEntity<ResponseObject> getInformation(@RequestHeader(name = "Authorization") String jwt) {
        try {

//        String jwt = parseJwt(request); // Lấy jwt từ request
            String token = jwt.substring(7, jwt.length());
            // Lấy email từ chuỗi jwt
            String email = Jwts.parser().setSigningKey("khanh").parseClaimsJws(token).getBody().getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            Users user = userService.findByEmail(userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Successfully!", new UserResponse(
                            user.getId(),
                            user.getFullName(),
                            user.getEmail(),
                            roles))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", "Error!",e.getMessage()));
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }

    // get all user
    @GetMapping
    public ResponseEntity<ResponseObject> getAllUser(@RequestParam(name = "roleid") Long roleId){
        try {
            List<Users> users = userService.findByRoles_Id(roleId);
            if(users != null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Successfully!", users)
                );
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("FAILED", "User is not exists!", null)
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", "Error from Exception!", e.getMessage())
            );
        }
    }
}
