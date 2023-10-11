package com.tddstudy.kiosk.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ProductType {

    HANDMADE("제조 음료"),
    BOTTLE("병 음료"),
    BAKERY("베이커리")
    ;

    private final String text;

    public static boolean containsStockType(ProductType productType) {
        return List.of(ProductType.BOTTLE, ProductType.BAKERY)
                .contains(productType);
    }
}
