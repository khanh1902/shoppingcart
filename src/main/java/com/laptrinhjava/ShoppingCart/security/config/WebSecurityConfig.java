package com.laptrinhjava.ShoppingCart.security.config;

import com.laptrinhjava.ShoppingCart.security.jwt.AuthEntryPointJwt;
import com.laptrinhjava.ShoppingCart.security.jwt.AuthTokenFilter;
import com.laptrinhjava.ShoppingCart.security.oauth2.CustomOAuth2AuthenticationEntryPoint;
import com.laptrinhjava.ShoppingCart.security.oauth2.CustomOAuth2UserService;
import com.laptrinhjava.ShoppingCart.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.laptrinhjava.ShoppingCart.security.oauth2.OAuth2LoginSuccessHandle;
import com.laptrinhjava.ShoppingCart.security.service.UserDetailsServiceImpl;
import com.laptrinhjava.ShoppingCart.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private CustomOAuth2UserService oauthUserService;

    @Qualifier("userServiceImpl")
    @Autowired
    private IUserService userService;

    @Autowired
    private OAuth2LoginSuccessHandle oAuth2LoginSuccessHandle;

    @Autowired
    private OAuth2AuthenticationFailureHandler auth2AuthenticationFailureHandler;

    @Autowired
    private CustomOAuth2AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DefaultCookieSerializer defaultCookieSerializer() {
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        defaultCookieSerializer.setCookieName("JSESSIONID");
        defaultCookieSerializer.setUseHttpOnlyCookie(true);
        defaultCookieSerializer.setCookiePath("/");
        defaultCookieSerializer.setUseSecureCookie(true);
        defaultCookieSerializer.setSameSite("None");
        return defaultCookieSerializer;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests().antMatchers("/api/auth/**", "/login").permitAll()
                .antMatchers("/api/**").permitAll()
                .anyRequest().authenticated();

//                .and()
//
//                .oauth2Login()
//                .authorizationEndpoint()
//                    .baseUri("/oauth2/authorize")
//                    .and()
//
//                .redirectionEndpoint()
//                    .baseUri("/oauth2/callback/*")
//                    .and()
//
//                .userInfoEndpoint()
//                    .userService(oauthUserService)
//                    .and()
//
//                .successHandler(oAuth2LoginSuccessHandle)
//                .failureHandler(auth2AuthenticationFailureHandler).and()
//                .exceptionHandling()
//                    .defaultAuthenticationEntryPointFor(new CustomOAuth2AuthenticationEntryPoint(), new AntPathRequestMatcher("/oauth2/**"));
//
//        http
//                .logout(l -> l
//                        .logoutSuccessUrl("/").permitAll()
//                );
//
//        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    //https://www.devglan.com/spring-security/spring-boot-security-google-oauth
    //https://www.devglan.com/spring-security/spring-security-oauth2-user-registration
}
