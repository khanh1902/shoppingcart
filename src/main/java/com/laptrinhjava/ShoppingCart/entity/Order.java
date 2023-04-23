package com.laptrinhjava.ShoppingCart.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

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

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Reviews> reviews;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItems> orderItems;


    public Order(Users users, String fullName, String email, Double totalPrice, Address address, String phoneNumber, String status) {
        this.users = users;
        this.fullName = fullName;
        this.email = email;
        this.totalPrice = totalPrice;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }
}
