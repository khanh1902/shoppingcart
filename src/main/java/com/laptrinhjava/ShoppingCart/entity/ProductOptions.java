package com.laptrinhjava.ShoppingCart.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "product_options")
@Getter
@Setter
@NoArgsConstructor
public class ProductOptions {
    @EmbeddedId
    ProductOptionsKey id;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    Products products;

    @ManyToOne
    @JoinColumn(name = "option_id", insertable = false, updatable = false)
    Options options;

    public ProductOptions(Products products, Options options) {
        this.products = products;
        this.options = options;
    }
}

