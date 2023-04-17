package com.laptrinhjava.ShoppingCart.payload.request.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderRequest {
    private List<Long> cartItemIds;
    private String email;
    private String fullName;
    private String province;
    private String district;
    private String ward;
    private String addressDetail;
    private String phoneNumber;
}
