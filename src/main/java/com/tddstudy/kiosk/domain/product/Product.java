package com.tddstudy.kiosk.domain.product;

import com.tddstudy.kiosk.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private ProductSellingType sellingType;

    private String name;

    private int price;

    @Builder
    public Product(Long id, ProductType type, ProductSellingType sellingType, String name, int price) {
        this.id = id;
        this.type = type;
        this.sellingType = sellingType;
        this.name = name;
        this.price = price;
    }
}
