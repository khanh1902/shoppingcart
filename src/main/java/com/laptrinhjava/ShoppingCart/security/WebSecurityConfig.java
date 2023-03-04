package com.laptrinhjava.ShoppingCart.security;

import com.laptrinhjava.ShoppingCart.security.jwt.AuthEntryPointJwt;
import com.laptrinhjava.ShoppingCart.security.jwt.AuthTokenFilter;
import com.laptrinhjava.ShoppingCart.security.oauth2.CustomOAuth2UserService;
import com.laptrinhjava.ShoppingCart.security.oauth2.OAuth2LoginSuccessHandle;
import com.laptrinhjava.ShoppingCart.security.service.UserDetailsServiceImpl;
import com.laptrinhjava.ShoppingCart.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig{
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private CustomOAuth2UserService oauthUserService;

    @Autowired
    private IUserService userService;

    @Autowired
    private OAuth2LoginSuccessHandle oAuth2LoginSuccessHandle;

    // fix cros
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://shopping-cart-ofji.onrender.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
//                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/auth/**", "/login").permitAll()
                .antMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                    .userService(oauthUserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandle);


        http.formLogin((form) -> form
                .loginPage("/login")
                .permitAll())
        ;

        http
                .logout(l -> l
                        .logoutSuccessUrl("/").permitAll()
                );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    //https://www.devglan.com/spring-security/spring-boot-security-google-oauth
    //https://www.devglan.com/spring-security/spring-security-oauth2-user-registration
}
