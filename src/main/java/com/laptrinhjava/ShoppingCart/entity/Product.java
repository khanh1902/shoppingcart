package com.laptrinhjava.ShoppingCart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Long price;

    @Column(name = "imageurl")
    private String imageUrl;

    @Column(name = "categoryid")
    private Long categoryId;

    @Column(name = "discountid")
    private Long discountId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createddate", nullable = false)
    private Date createdDate;

    @PrePersist
    private void onCreated() {
        createdDate = new Date();
    }

    //    @JsonIgnore
//    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
//    private List<CartDetails> cards = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartItems> cartItems;

    public Product() {
    }

    public Product(String name, Long price, String imageUrl, Long categoryId, Long discountId) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.discountId = discountId;
    }
}
