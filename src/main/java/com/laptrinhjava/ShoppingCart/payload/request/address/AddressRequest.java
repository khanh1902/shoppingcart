package com.laptrinhjava.ShoppingCart.payload.request.address;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
    private String province;
    private String district;
    private String ward;
    private String addressDetail;
    private String isDefault;
}
