package com.laptrinhjava.ShoppingCart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reviews")
@Setter
@Getter
@NoArgsConstructor
public class Reviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createddate", nullable = false)
    private Date createdDate;

    @PrePersist
    private void onCreated() {
        createdDate = new Date();
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products products;

    @Column(name = "is_review")
    private Boolean isReview;

    public Reviews(Integer rating, String description, Date createdDate, Users users, Products products, Boolean isReview) {
        this.rating = rating;
        this.description = description;
        this.createdDate = createdDate;
        this.users = users;
        this.products = products;
        this.isReview = isReview;
    }
}
