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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderRes createOrder(OrderCreateReq orderCreateReq, LocalDateTime registeredDateTime) {
        List<Product> duplicateProducts = findProductBy(orderCreateReq);

        Order order = Order.create(duplicateProducts, registeredDateTime);
        Order savedOrder = orderRepository.save(order);
        return OrderRes.of(savedOrder);
    }

    private List<Product> findProductBy(OrderCreateReq orderCreateReq) {
        List<Product> products = productRepository.findAllByProductNumberIn(orderCreateReq.getProductNumbers());

        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));
        return orderCreateReq.getProductNumbers().stream()
                .map(productMap::get)
                .collect(Collectors.toList());
    }
}
