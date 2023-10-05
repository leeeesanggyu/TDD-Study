package com.tddstudy.kiosk.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ProductSellingType {

    SELLING("판매중"),
    HOLD("판매 보류"),
    STOP_SELLING("판매 중지")
    ;

    private final String text;

    public static List<ProductSellingType> getDisplay() {
        return List.of(SELLING, HOLD);
    }
}
