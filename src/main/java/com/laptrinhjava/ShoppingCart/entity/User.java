package com.laptrinhjava.ShoppingCart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // quan hệ n-n với đối tượng ở dưới (Role) (1 user có nhiều quyền)
    // LAZY để tránh việc truy xuất dữ liệu không cần thiết. Lúc nào cần thì mới query
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "userid"), // khóa ngoại chính là userid trỏ tới class hiện tại (User)
            inverseJoinColumns = @JoinColumn(name = "roleid")) // Khóa ngoại thứ 2 trỏ tới thuộc tính ở dưới (Role)
    private Set<Role> roles = new HashSet<>();

    // constructor
    public User() {
    }

    public User(String userName, String password, String fullName, String email) {
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }
}

