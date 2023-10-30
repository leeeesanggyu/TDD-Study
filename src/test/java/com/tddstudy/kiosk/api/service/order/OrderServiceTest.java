package com.tddstudy.kiosk.api.service.order;

import com.tddstudy.kiosk.IntegrationTestSupport;
import com.tddstudy.kiosk.api.controller.order.req.OrderCreateReq;
import com.tddstudy.kiosk.api.service.order.res.OrderRes;
import com.tddstudy.kiosk.domain.order.Order;
import com.tddstudy.kiosk.domain.order.OrderRepository;
import com.tddstudy.kiosk.domain.orderproduct.OrderProductRepository;
import com.tddstudy.kiosk.domain.product.Product;
import com.tddstudy.kiosk.domain.product.ProductRepository;
import com.tddstudy.kiosk.domain.product.ProductSellingType;
import com.tddstudy.kiosk.domain.product.ProductType;
import com.tddstudy.kiosk.domain.stock.Stock;
import com.tddstudy.kiosk.domain.stock.StockRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

class OrderServiceTest extends IntegrationTestSupport {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    StockRepository stockRepository;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
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

        List<Product> products = List.of(
                createProduct("001", ProductType.HANDMADE),
                createProduct("002", ProductType.HANDMADE),
                createProduct("003", ProductType.HANDMADE)
        );        productRepository.saveAll(products);

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

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrderWithStock() {
        LocalDateTime registeredDateTime = LocalDateTime.now();

        List<Product> products = List.of(
                createProduct("001", ProductType.BOTTLE),
                createProduct("002", ProductType.BAKERY),
                createProduct("003", ProductType.HANDMADE)
        );
        productRepository.saveAll(products);

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateReq orderCreateReq = OrderCreateReq.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();
        OrderRes orderRes = orderService.createOrder(orderCreateReq, registeredDateTime);

        Assertions.assertThat(orderRes.getId()).isNotNull();
        Assertions.assertThat(orderRes)
                .extracting("totalPrice", "registeredDateTime")
                .containsExactly(8000, registeredDateTime);
        Assertions.assertThat(orderRes.getProducts()).hasSize(4)
                .extracting("productNumber")
                .containsExactlyInAnyOrder("001", "001", "002", "003");

        List<Stock> stocks = stockRepository.findAll();
        Assertions.assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", 0),
                        Tuple.tuple("002", 1)
                );
    }

    @DisplayName("재고가 부족한 상품으로 주문을 생성하면 예외가 발생한다.")
    @Test
    void createOrderWithNotEnoughStock() {
        LocalDateTime registeredDateTime = LocalDateTime.now();

        List<Product> products = List.of(
                createProduct("001", ProductType.BOTTLE),
                createProduct("002", ProductType.BAKERY),
                createProduct("003", ProductType.HANDMADE)
        );
        productRepository.saveAll(products);

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 0);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateReq orderCreateReq = OrderCreateReq.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();

        Assertions.assertThatThrownBy(() -> orderService.createOrder(orderCreateReq, registeredDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고보다 주문 수량이 더 많습니다.");
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