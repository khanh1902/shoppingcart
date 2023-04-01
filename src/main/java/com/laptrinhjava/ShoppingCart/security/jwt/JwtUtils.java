package com.laptrinhjava.ShoppingCart.security.jwt;

import com.laptrinhjava.ShoppingCart.entity.Users;
import com.laptrinhjava.ShoppingCart.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/*
 * Sau khi có các thông tin về người dùng, chúng ta cần mã hóa thông tin người dùng thành chuỗi JWT.
 * */
@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
    private final String jwtSecret = "khanh";

    // Thời gian có hiệu lực của chuỗi jwt
    private final long jwtExpiration = 3600000L * 2L * 24L;  //hạn chuỗi jwt 1 ngay

    // Tạo jwt
    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Date now = new Date(); // thời gian tạo
        Date expiryDate = new Date(now.getTime() + jwtExpiration); // thời gian hết hạn
        // Tạo chuỗi json web token từ userName của user.
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // cấp phát jwt cho user
                .setIssuedAt(now) // thời gian cấp
                .setExpiration(expiryDate) // thời gian hết hạn
                .signWith(SignatureAlgorithm.HS512, jwtSecret) //ký tên
                .compact(); // thực thi
    }

    public String generateTokenForOAuth2(Users user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());

        Date now = new Date(); // thời gian tạo
        Date expiryDate = new Date(now.getTime() + jwtExpiration); // thời gian hết hạn

        return Jwts.builder()
                .setSubject(user.getEmail()) // cấp phát jwt cho user
                .setIssuedAt(now) // thời gian cấp
                .setExpiration(expiryDate) // thời gian hết hạn
                .signWith(SignatureAlgorithm.HS512, jwtSecret) //ký tên
                .compact(); // thực thi
    }

    // Lấy thông tin user từ jwt
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    // xác thực jwt
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }
}
