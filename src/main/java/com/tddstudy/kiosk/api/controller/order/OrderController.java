package com.tddstudy.kiosk.api.controller.order;

import com.tddstudy.kiosk.api.service.order.OrderService;
import com.tddstudy.kiosk.api.service.order.req.OrderCreateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public void createOrder(
            @RequestBody OrderCreateReq orderCreateReq
    ) {
        LocalDateTime registeredDateTime = LocalDateTime.now();
        orderService.createOrder(orderCreateReq, registeredDateTime);
    }
}
