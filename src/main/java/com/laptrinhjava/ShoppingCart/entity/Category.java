package com.laptrinhjava.ShoppingCart.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "category")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "imageurl")
    private String imageUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createddate", nullable = false)
    private Date createdDate;
    @PrePersist
    private void onCreated(){
        createdDate = new Date();
    }

    public Category() {
    }

    public Category(String name, String code, String imageUrl) {
        this.name = name;
        this.code = code;
        this.imageUrl = imageUrl;
    }
}
