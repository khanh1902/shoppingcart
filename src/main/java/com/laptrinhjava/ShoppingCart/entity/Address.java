package com.laptrinhjava.ShoppingCart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "address")
@Setter
@Getter
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "province")
    private String province;

    @Column(name = "district")
    private String district;

    @Column(name = "ward")
    private String ward;

    @Column(name = "addressdetail")
    private String addressDetail;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @Column(name = "is_default")
    private Boolean isDefault;

    public Address(String province, String district, String ward, String addressDetail, Users users, Boolean isDefault) {
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.addressDetail = addressDetail;
        this.users = users;
        this.isDefault = isDefault;
    }
}
