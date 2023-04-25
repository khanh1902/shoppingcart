package com.laptrinhjava.ShoppingCart.payload.response.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentResponse {
    private Long paymentId;
    private String paymentCode;
    private String paymentName;
}
