package com.slavamashkov.shop;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@RequiredArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderKey implements Serializable {

    static final long serialVersionUID = 1L;

    @NonNull
    @Column(name = "customer_id")
    private Integer customerId;

    @NonNull
    @Column(name = "product_id")
    private Integer productId;
}
