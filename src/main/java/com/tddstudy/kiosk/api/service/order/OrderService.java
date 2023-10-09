package com.tddstudy.kiosk.api.service.order;

import com.tddstudy.kiosk.api.service.order.req.OrderCreateReq;
import com.tddstudy.kiosk.api.service.order.res.OrderRes;
import com.tddstudy.kiosk.domain.order.Order;
import com.tddstudy.kiosk.domain.order.OrderRepository;
import com.tddstudy.kiosk.domain.product.Product;
import com.tddstudy.kiosk.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderRes createOrder(OrderCreateReq orderCreateReq, LocalDateTime registeredDateTime) {
        List<Product> products = productRepository.findAllByIdIn(orderCreateReq.getProductId());

        Order order = Order.create(products, registeredDateTime);
        Order savedOrder = orderRepository.save(order);
        return OrderRes.of(savedOrder);
    }
}
