package com.laptrinhjava.ShoppingCart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
public class CartItems {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cardid")
    private Cart cart;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "price")
    private Long price;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createddate", nullable = false)
    private Date createdDate;
    @PrePersist
    private void onCreated(){
        createdDate = new Date();
    }

    public CartItems() {
    }

    public CartItems(Cart cart, Product product, Long quantity, Long price) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public CartItems(Long id, Cart cart, Product product) {
        this.id = id;
        this.cart = cart;
        this.product = product;
    }
}
