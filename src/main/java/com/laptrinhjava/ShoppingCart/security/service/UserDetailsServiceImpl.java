package com.laptrinhjava.ShoppingCart.security.service;


import com.laptrinhjava.ShoppingCart.entity.User;
import com.laptrinhjava.ShoppingCart.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
Khi người dùng đăng nhập thì Spring Security sẽ cần lấy các thông tin UserDetails hiện có để kiểm tra.
Vì vậy, bạn cần tạo ra một class kế thừa lớp UserDetailsService mà Spring Security cung cấp để làm nhiệm vụ này.
*/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // user not found -> get message
        User user = userService.findUserName(userName).
                orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userName));
        return UserDetailsImpl.build(user);
    }
}
