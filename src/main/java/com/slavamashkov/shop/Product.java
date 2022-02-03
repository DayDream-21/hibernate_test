package com.slavamashkov.shop;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "products", schema = "shop")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NonNull
    @Column(name = "name", nullable = false, unique = true, length = 30)
    private String name;

    @NonNull
    @Column(name = "price", nullable = false, precision = 4, scale = 2)
    private BigDecimal price;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Order> orders;
}