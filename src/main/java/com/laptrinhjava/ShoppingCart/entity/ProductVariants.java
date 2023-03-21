package com.laptrinhjava.ShoppingCart.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "product_variants")
@NoArgsConstructor
public class ProductVariants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "sku_id")
    private String skuId;

    @Column(name = "price", nullable = true)
    private Long price;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id") // thông qua khóa ngoại address_id
    private Products products;

    public ProductVariants(String skuId, Long price, Products products) {
        this.skuId = skuId;
        this.price = price;
        this.products = products;
    }
}
