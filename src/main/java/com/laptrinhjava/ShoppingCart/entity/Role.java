package com.laptrinhjava.ShoppingCart.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private ERole name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    // LAZY để tránh việc truy xuất dữ liệu không cần thiết. Lúc nào cần thì mới query
    private List<User> users = new ArrayList<>();

    public Role() {
    }

    public Role(ERole name) {
        this.name = name;
    }
}
