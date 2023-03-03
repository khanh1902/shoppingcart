package com.laptrinhjava.ShoppingCart.security.oauth2;

import com.laptrinhjava.ShoppingCart.entity.User;
import com.laptrinhjava.ShoppingCart.security.jwt.JwtUtils;
import com.laptrinhjava.ShoppingCart.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OAuth2LoginSuccessHandle extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        userService.processOAuthPostLogin(oAuth2User);
        User user = userService.findByEmail(email);
        String jwt = jwtUtils.generateTokenForOAuth2(user);

        List<String> roles = oAuth2User.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());


        String redirectionUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/api/product/findAll")
                .queryParam("auth_token", jwt)
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, redirectionUrl);

        System.out.println("Email : "  + email);
        System.out.println("Token : "  + jwt);
    }
}
