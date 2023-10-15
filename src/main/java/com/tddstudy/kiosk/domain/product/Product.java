package com.tddstudy.kiosk.domain.product;

import com.tddstudy.kiosk.BaseEntity;
import com.tddstudy.kiosk.api.service.product.res.ProductRes;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private ProductSellingType sellingType;

    private String name;

    private int price;

    @Builder
    public Product(String productNumber, ProductType type, ProductSellingType sellingType, String name, int price) {
        this.productNumber = productNumber;
        this.type = type;
        this.sellingType = sellingType;
        this.name = name;
        this.price = price;
    }
}
