package com.slavamashkov.shop;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
@ToString
public class OrderKey implements Serializable {

    static final long serialVersionUID = 1L;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "product_id")
    private Integer productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderKey orderKey = (OrderKey) o;
        return Objects.equals(customerId, orderKey.customerId) && Objects.equals(productId, orderKey.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, productId);
    }
}
