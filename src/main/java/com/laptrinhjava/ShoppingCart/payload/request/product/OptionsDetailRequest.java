package com.laptrinhjava.ShoppingCart.payload.request.product;

import com.laptrinhjava.ShoppingCart.payload.response.product.TypeOptionsDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OptionsDetailRequest {
    private List<TypeOptionsDetail> options;
    public OptionsDetailRequest(List<TypeOptionsDetail> options) {
        this.options = options;
    }
}
