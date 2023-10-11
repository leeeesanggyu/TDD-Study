package com.tddstudy.kiosk.api.service.order;

import com.tddstudy.kiosk.api.service.order.req.OrderCreateReq;
import com.tddstudy.kiosk.api.service.order.res.OrderRes;
import com.tddstudy.kiosk.domain.order.OrderRepository;
import com.tddstudy.kiosk.domain.orderproduct.OrderProductRepository;
import com.tddstudy.kiosk.domain.product.Product;
import com.tddstudy.kiosk.domain.product.ProductRepository;
import com.tddstudy.kiosk.domain.product.ProductSellingType;
import com.tddstudy.kiosk.domain.product.ProductType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
        LocalDateTime registeredDateTime = LocalDateTime.now();

        List<Product> products = List.of(createProduct("001"), createProduct("002"), createProduct("003"));
        productRepository.saveAll(products);

        OrderCreateReq orderCreateReq = OrderCreateReq.builder()
                .productNumbers(List.of("001", "002", "003"))
                .build();

        OrderRes orderRes = orderService.createOrder(orderCreateReq, registeredDateTime);

        Assertions.assertThat(orderRes.getId()).isNotNull();
        Assertions.assertThat(orderRes)
                .extracting("totalPrice", "registeredDateTime")
                .containsExactly(6000, registeredDateTime);
        Assertions.assertThat(orderRes.getProducts()).hasSize(3)
                .extracting("productNumber")
                .containsExactlyInAnyOrder("001", "002", "003");
    }

    @DisplayName("중복되는 주문번호 리스트를 받아 주문을 생성할 수 있다.")
    @Test
    void createOrderDuplicateProductId() {
        LocalDateTime registeredDateTime = LocalDateTime.now();

        List<Product> products = List.of(createProduct("001"), createProduct("002"), createProduct("003"));
        productRepository.saveAll(products);

        List<Product> all2 = productRepository.findAll();
        System.out.println("result => " + all2);

        OrderCreateReq orderCreateReq = OrderCreateReq.builder()
                .productNumbers(List.of("001", "001", "003"))
                .build();
        OrderRes orderRes = orderService.createOrder(orderCreateReq, registeredDateTime);

        Assertions.assertThat(orderRes.getId()).isNotNull();
        Assertions.assertThat(orderRes)
                .extracting("totalPrice", "registeredDateTime")
                .containsExactly(6000, registeredDateTime);
        Assertions.assertThat(orderRes.getProducts()).hasSize(3)
                .extracting("productNumber")
                .containsExactlyInAnyOrder("001", "001", "003");

    }

    private Product createProduct(String productNumber) {
        return Product.builder()
                .productNumber(productNumber)
                .type(ProductType.HANDMADE)
                .sellingType(ProductSellingType.SELLING)
                .name("아메리카노")
                .price(2000)
                .build();
    }
}