package com.laptrinhjava.ShoppingCart.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "password_reset_token")
@Setter
@Getter
@NoArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    private Date expiryDate;
    @PrePersist
    private void onCreated(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, 5);
        expiryDate = cal.getTime();
    }

    public PasswordResetToken(String token, Users user) {
        this.token = token;
        this.user = user;
    }
}
