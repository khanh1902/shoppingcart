package com.laptrinhjava.ShoppingCart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "option_values")
@NoArgsConstructor
public class OptionValues implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "option_id")
    private Options option;

    public OptionValues(String name, Options option) {
        this.name = name;
        this.option = option;
    }
}
