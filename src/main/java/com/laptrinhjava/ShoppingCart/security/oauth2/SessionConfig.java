package com.laptrinhjava.ShoppingCart.security.oauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class SessionConfig {
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        defaultCookieSerializer.setCookieName("JSESSIONID");
        defaultCookieSerializer.setUseHttpOnlyCookie(true);
        defaultCookieSerializer.setCookiePath("/");
        defaultCookieSerializer.setUseSecureCookie(true);
        defaultCookieSerializer.setSameSite("None");
        return defaultCookieSerializer;
    }
}