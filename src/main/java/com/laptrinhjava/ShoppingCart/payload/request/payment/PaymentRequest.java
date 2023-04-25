package com.laptrinhjava.ShoppingCart.payload.request.payment;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class PaymentRequest {
    int amount;
    String description;
    String bankCode;
}
