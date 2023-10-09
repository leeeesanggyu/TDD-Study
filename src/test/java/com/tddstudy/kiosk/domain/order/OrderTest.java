package com.tddstudy.kiosk.domain.order;

import com.tddstudy.kiosk.domain.product.Product;
import com.tddstudy.kiosk.domain.product.ProductSellingType;
import com.tddstudy.kiosk.domain.product.ProductType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @DisplayName("주문 생성시 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calculateTotalPrice() {
        List<Product> products = List.of(
                createProduct(1L, 1000),
                createProduct(2L, 2000),
                createProduct(3L, 3000)
        );

        Order order = Order.create(products, LocalDateTime.now());

        Assertions.assertThat(order.getTotalPrice()).isEqualTo(6000);
    }

    @DisplayName("주문 생성시 주문 상태는 INIT 이다.")
    @Test
    void orderInit() {
        List<Product> products = List.of(
                createProduct(1L, 1000),
                createProduct(2L, 2000),
                createProduct(3L, 3000)
        );

        Order order = Order.create(products, LocalDateTime.now());

        Assertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.INIT);
    }

    @DisplayName("주문 생성시 주문시간을 넣어준다.")
    @Test
    void registeredDateTime() {
        LocalDateTime registeredDateTime = LocalDateTime.now();

        List<Product> products = List.of(
                createProduct(1L, 1000),
                createProduct(2L, 2000),
                createProduct(3L, 3000)
        );

        Order order = Order.create(products, registeredDateTime);

        Assertions.assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    }

    private Product createProduct(Long id, int price) {
        return Product.builder()
                .id(id)
                .type(ProductType.HANDMADE)
                .sellingType(ProductSellingType.SELLING)
                .name("아메리카노")
                .price(price)
                .build();
    }
}