package com.laptrinhjava.ShoppingCart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "options")
@NoArgsConstructor
public class Options implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL)
    private List<OptionValues> optionValues;

//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "optionValues_id")
//    private OptionValues optionValues;

    @JsonIgnore
    @OneToMany(mappedBy = "options", cascade = CascadeType.ALL)
    private List<ProductOptions> productOptions;

    public Options(String name) {
        this.name = name;
    }
}
