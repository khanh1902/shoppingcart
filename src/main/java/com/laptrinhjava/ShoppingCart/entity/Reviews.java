package com.laptrinhjava.ShoppingCart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
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

    @Column(name = "rating", nullable = true)
    private Integer rating;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createddate", nullable = true)
    private Date createdDate;

//    @PrePersist
//    private void onCreated() {
//        Date date = new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        createdDate = cal.getTime();
//    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products products;

    @Column(name = "is_review")
    private Boolean isReview;

    public Reviews(Integer rating, String description,  Users users, Order order, Products products, Boolean isReview) {
        this.rating = rating;
        this.description = description;
        this.users = users;
        this.order = order;
        this.products = products;
        this.isReview = isReview;
    }
}
