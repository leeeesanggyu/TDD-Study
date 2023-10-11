package com.tddstudy.kiosk.api.service.order.req;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateReq {
    private List<String> productNumbers;

    @Builder
    private OrderCreateReq(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }
}
