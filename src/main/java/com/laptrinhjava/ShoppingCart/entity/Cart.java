//package com.laptrinhjava.ShoppingCart.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.util.Date;
//import java.util.List;
//
//@Entity
//@Table(name = "cart")
//@Getter
//@Setter
//public class Cart {
//    @Id
//    @Column(name = "id")
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "userid")
//    private Long userId;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "createddate", nullable = false)
//    private Date createdDate;
//    @PrePersist
//    private void onCreated(){
//        createdDate = new Date();
//    }
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
//    private List<CartItems> cartItems;
//
////    @JsonIgnore
////    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
////    @JoinTable(name = "cart_items",
////            joinColumns = @JoinColumn(name = "cardid"),
////            inverseJoinColumns = @JoinColumn(name = "productid"))
////    private List<Product> products = new ArrayList<>();
//
//    public Cart() {
//    }
//
//    public Cart(Long id, Long userId, List<CartItems> cartItems) {
//        this.id = id;
//        this.userId = userId;
//        this.cartItems = cartItems;
//    }
//}
