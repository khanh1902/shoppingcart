package com.laptrinhjava.ShoppingCart.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "category")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private void onCreated(){
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Long discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
