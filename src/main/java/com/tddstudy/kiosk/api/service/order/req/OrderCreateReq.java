package com.tddstudy.kiosk.api.service.order.req;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateReq {
    private List<Long> productId;

    @Builder
    private OrderCreateReq(List<Long> productId) {
        this.productId = productId;
    }
}
