package com.tddstudy.kiosk.api.service.order.res;

import com.tddstudy.kiosk.api.service.product.res.ProductRes;
import com.tddstudy.kiosk.domain.order.Order;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderRes {

    private Long id;
    private int totalPrice;
    private LocalDateTime registeredDateTime;
    private List<ProductRes> products;

    @Builder
    private OrderRes(Long id, int totalPrice, LocalDateTime registeredDateTime, List<ProductRes> products) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.registeredDateTime = registeredDateTime;
        this.products = products;
    }

    public static OrderRes of(Order order) {
        return OrderRes.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .registeredDateTime(order.getRegisteredDateTime())
                .products(order.getOrderProducts().stream()
                        .map(orderProduct -> ProductRes.of(orderProduct.getProduct()))
                        .collect(Collectors.toList()))
                .build();
    }
}
