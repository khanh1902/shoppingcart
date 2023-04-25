package com.laptrinhjava.ShoppingCart.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
public
class ProductOptionsKey implements Serializable {
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "option_id")
    private Long optionId;

    public ProductOptionsKey(Long productId, Long optionId) {
        this.productId = productId;
        this.optionId = optionId;
    }
}
