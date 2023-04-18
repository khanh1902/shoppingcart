package com.laptrinhjava.ShoppingCart.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "password_reset_token")
@Setter
@Getter
@NoArgsConstructor
public class PasswordResetToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    private Date expiryDate;

    public PasswordResetToken(String token, Users user) {
        this.token = token;
        this.user = user;
    }
}
