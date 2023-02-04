package com.laptrinhjava.ShoppingCart.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "discount")
@Getter
@Setter
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = " description")
    private String description;

    @Column(name = "discount_percent")
    private Long discountPercent;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createddate", nullable = false)
    private Date createdDate;

    @PrePersist
    private void onCreated() {
        createdDate = new Date();
    }

    public Discount() {
    }

    public Discount(String name, String description, Long discountPercent, Date createdDate) {
        this.name = name;
        this.description = description;
        this.discountPercent = discountPercent;
        this.createdDate = createdDate;
    }
}
