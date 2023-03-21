package com.laptrinhjava.ShoppingCart.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
public
class VariantValuesKey implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "variant_id")
    private Long variantId;

    @Column(name = "option_id")
    private Long optionId;

    @Column(name = "value_id")
    private Long valueId;

    public VariantValuesKey(Long productId, Long variantId, Long optionId, Long valueId) {
        this.productId = productId;
        this.variantId = variantId;
        this.optionId = optionId;
        this.valueId = valueId;
    }
}
