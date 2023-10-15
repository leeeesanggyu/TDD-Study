package com.tddstudy.kiosk.api.service.product.req;

import com.tddstudy.kiosk.domain.product.Product;
import com.tddstudy.kiosk.domain.product.ProductSellingType;
import com.tddstudy.kiosk.domain.product.ProductType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductCreateReq {

    private ProductType type;
    private ProductSellingType sellingType;
    private String name;
    private int price;

    @Builder
    private ProductCreateReq(ProductType type, ProductSellingType sellingType, String name, int price) {
        this.type = type;
        this.sellingType = sellingType;
        this.name = name;
        this.price = price;
    }

    public Product of(String productNumber) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingType(sellingType)
                .name(name)
                .price(price)
                .build();
    }
}
