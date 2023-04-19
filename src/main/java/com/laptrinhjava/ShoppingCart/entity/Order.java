package com.laptrinhjava.ShoppingCart.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users users;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "fullname")
    private String fullName;

    @Column (name = "email")
    private String email;

    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "phonenumber")
    private String phoneNumber;

    @Column(name = "status")
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createddate", nullable = false)
    private Date createdDate;

    @PrePersist
    private void onCreated(){
        createdDate = new Date();
    }


    public Order(Users users, String fullName, String email, Double totalPrice, Long addressId, String phoneNumber, String status) {
        this.users = users;
        this.fullName = fullName;
        this.email = email;
        this.totalPrice = totalPrice;
        this.addressId = addressId;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }
}
