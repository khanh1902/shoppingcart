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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
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

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService). // Cung cấp userDetailsService cho spring security
                passwordEncoder(passwordEncoder()); // // cung cấp password encoder
    }

    // fix cros
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://shopping-cart-ofji.onrender.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "content-type"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "content-type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable() // chặn request từ một domain khác
                .authorizeRequests()
                .antMatchers("/", "/login", "/api/auth/**", "/oauth/**", "/api/product/**").permitAll() // cho phép tất cả truy cập
//                .antMatchers("/api/admin/**").hasRole("ADMIN")
//                .antMatchers("/api/**").hasAnyRole("USER", "ADMIN")
//                .antMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
                .and()
//                .authorizeHttpRequests()
//                .httpBasic()//login basic
//                .and()
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

        // Thêm một lớp Filter kiểm tra jwt
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    //https://www.devglan.com/spring-security/spring-boot-security-google-oauth
    //https://www.devglan.com/spring-security/spring-security-oauth2-user-registration
}
