package com.tddstudy.kiosk.api.service.product.response;

import com.tddstudy.kiosk.product.Product;
import com.tddstudy.kiosk.product.ProductSellingType;
import com.tddstudy.kiosk.product.ProductType;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
public class ProductRes {

    private Long id;
    private ProductType type;
    private ProductSellingType sellingType;
    private String name;
    private int price;

    @Builder
    private ProductRes(Long id, ProductType type, ProductSellingType sellingType, String name, int price) {
        this.id = id;
        this.type = type;
        this.sellingType = sellingType;
        this.name = name;
        this.price = price;
    }

    public static ProductRes of(Product product) {
        return ProductRes.builder()
                .id(product.getId())
                .type(product.getType())
                .sellingType(product.getSellingType())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
