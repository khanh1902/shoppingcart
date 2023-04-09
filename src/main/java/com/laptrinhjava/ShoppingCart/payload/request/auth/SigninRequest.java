package com.laptrinhjava.ShoppingCart.payload.request.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SigninRequest {

    /*
        @NotBlank: Annotation này từ chối String có giá trị null và String có độ dài là 0 sau khi đã trim (loại
        bỏ hết khoảng trắng thừa ở đầu và cuối của String).
    */
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
