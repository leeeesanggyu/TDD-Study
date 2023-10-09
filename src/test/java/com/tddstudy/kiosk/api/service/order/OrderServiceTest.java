package com.tddstudy.kiosk.api.service.order;

import com.tddstudy.kiosk.api.service.order.req.OrderCreateReq;
import com.tddstudy.kiosk.api.service.order.res.OrderRes;
import com.tddstudy.kiosk.domain.product.Product;
import com.tddstudy.kiosk.domain.product.ProductRepository;
import com.tddstudy.kiosk.domain.product.ProductSellingType;
import com.tddstudy.kiosk.domain.product.ProductType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
        LocalDateTime registeredDateTime = LocalDateTime.now();

        List<Product> products = List.of(createProduct(1L), createProduct(2L), createProduct(3L));
        productRepository.saveAll(products);

        OrderCreateReq orderCreateReq = OrderCreateReq.builder()
                .productId(List.of(1L, 2L, 3L))
                .build();

        OrderRes orderRes = orderService.createOrder(orderCreateReq, registeredDateTime);

        Assertions.assertThat(orderRes.getId()).isNotNull();
        Assertions.assertThat(orderRes)
                .extracting("totalPrice", "registeredDateTime")
                .containsExactly(6000, registeredDateTime);
        Assertions.assertThat(orderRes.getProducts()).hasSize(3)
                .extracting("id")
                .containsExactlyInAnyOrder(1L, 2L, 3L);

    }

    private Product createProduct(Long id) {
        return Product.builder()
                .id(id)
                .type(ProductType.HANDMADE)
                .sellingType(ProductSellingType.SELLING)
                .name("아메리카노")
                .price(2000)
                .build();
    }
}