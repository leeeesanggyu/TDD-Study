package com.tddstudy.kiosk.domain.order;

import com.tddstudy.kiosk.api.controller.order.req.OrderCreateReq;
import com.tddstudy.kiosk.api.service.order.OrderService;
import com.tddstudy.kiosk.api.service.order.res.OrderRes;
import com.tddstudy.kiosk.domain.orderproduct.OrderProductRepository;
import com.tddstudy.kiosk.domain.product.Product;
import com.tddstudy.kiosk.domain.product.ProductRepository;
import com.tddstudy.kiosk.domain.product.ProductSellingType;
import com.tddstudy.kiosk.domain.product.ProductType;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.tddstudy.kiosk.domain.order.OrderStatus.COMPLETED;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderProductRepository orderProductRepository;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @DisplayName("특정 시간, 특정 주문 상태의 주문을 조회한다.")
    @Test
    void findOrdersBy() {
        List<Product> products = List.of(
                createProduct("001", ProductType.HANDMADE),
                createProduct("002", ProductType.HANDMADE),
                createProduct("003", ProductType.HANDMADE)
        );
        productRepository.saveAll(products);

        OrderCreateReq orderCreateReq = OrderCreateReq.builder()
                .productNumbers(List.of("001", "002", "003"))
                .build();

        OrderRes orderRes1 = orderService.createOrder(orderCreateReq, LocalDateTime.of(2023, 10, 16, 22, 31, 15));
        OrderRes orderRes2 = orderService.createOrder(orderCreateReq, LocalDateTime.of(2023, 10, 17, 22, 31, 15));
        OrderRes orderRes3 = orderService.createOrder(orderCreateReq, LocalDateTime.of(2023, 10, 18, 22, 31, 15));

        orderRepository.updateOrderStatusCompleted(orderRes1.getId());
        orderRepository.updateOrderStatusCompleted(orderRes2.getId());

        List<Order> orders = orderRepository.findOrdersBy(
                LocalDateTime.of(2023, 10, 17, 22, 31, 15),
                LocalDateTime.of(2023, 10, 18, 22, 31, 15),
                OrderStatus.PAYMENT_COMPLETED
        );

        Assertions.assertThat(orders)
                .extracting("id", "orderStatus", "registeredDateTime")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(orderRes2.getId(), OrderStatus.PAYMENT_COMPLETED, orderRes2.getRegisteredDateTime())
                );
    }

    @DisplayName("기존의 주문의 주문상태를 COMPLETED 상태로 변경한다.")
    @Test
    void updateOrderStatusCompleted() {
        LocalDateTime registeredDateTime = LocalDateTime.now();

        List<Product> products = List.of(
                createProduct("001", ProductType.HANDMADE),
                createProduct("002", ProductType.HANDMADE),
                createProduct("003", ProductType.HANDMADE)
        );
        productRepository.saveAll(products);

        OrderCreateReq orderCreateReq = OrderCreateReq.builder()
                .productNumbers(List.of("001", "002", "003"))
                .build();

        OrderRes orderRes = orderService.createOrder(orderCreateReq, registeredDateTime);

        long lows = orderRepository.updateOrderStatusCompleted(orderRes.getId());

        Order updatedOrderStatusOrder = orderRepository.findById(orderRes.getId()).get();
        Assertions.assertThat(lows).isEqualTo(1);
        Assertions.assertThat(updatedOrderStatusOrder.getOrderStatus()).isEqualTo(OrderStatus.PAYMENT_COMPLETED);
    }

    private Product createProduct(String productNumber, ProductType productType) {
        return Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .sellingType(ProductSellingType.SELLING)
                .name("아메리카노")
                .price(2000)
                .build();
    }
}