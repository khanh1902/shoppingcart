package com.laptrinhjava.ShoppingCart.payload.response.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CountOrderForUserResponse {
    private Long userId;
    private Long countOrder;

    public CountOrderForUserResponse(Long userId, Long countOrder) {
        this.userId = userId;
        this.countOrder = countOrder;
    }
}
