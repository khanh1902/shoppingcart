package com.laptrinhjava.ShoppingCart.security.oauth2;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLoginAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final String loginPageUrl;

    public CustomLoginAuthenticationEntryPoint(String loginPageUrl) {
        this.loginPageUrl = loginPageUrl;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // Redirect to the login page
        response.sendRedirect(request.getContextPath() + loginPageUrl);
    }
}

