package com.tddstudy.kiosk.api.service.order;

import com.tddstudy.kiosk.IntegrationTestSupport;
import com.tddstudy.kiosk.api.controller.order.req.OrderCreateReq;
import com.tddstudy.kiosk.api.service.order.res.OrderRes;
import com.tddstudy.kiosk.client.mail.MailClient;
import com.tddstudy.kiosk.domain.history.mail.MailSendHistory;
import com.tddstudy.kiosk.domain.history.mail.MailSendHistoryRepository;
import com.tddstudy.kiosk.domain.order.Order;
import com.tddstudy.kiosk.domain.order.OrderRepository;
import com.tddstudy.kiosk.domain.order.OrderStatus;
import com.tddstudy.kiosk.domain.orderproduct.OrderProductRepository;
import com.tddstudy.kiosk.domain.product.Product;
import com.tddstudy.kiosk.domain.product.ProductRepository;
import com.tddstudy.kiosk.domain.product.ProductSellingType;
import com.tddstudy.kiosk.domain.product.ProductType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class OrderStatisticsServiceTest extends IntegrationTestSupport {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    MailSendHistoryRepository mailSendHistoryRepository;

//    @MockBean
//    MailClient mailClient;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @DisplayName("결제 완료 상태의 주문들의 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatisticsMail() {
        LocalDateTime nowDateTime = LocalDateTime.of(2023, 10, 17, 22, 31, 22);

        List<Product> products = List.of(
                createProduct("001", ProductType.HANDMADE),
                createProduct("002", ProductType.HANDMADE),
                createProduct("003", ProductType.HANDMADE)
        );
        productRepository.saveAll(products);

        OrderCreateReq orderCreateReq = OrderCreateReq.builder()
                .productNumbers(List.of("001", "002", "003"))
                .build();

        OrderRes orderRes1 = orderService.createOrder(orderCreateReq, LocalDateTime.of(2023, 10, 16, 23, 59, 59));
        OrderRes orderRes2 = orderService.createOrder(orderCreateReq, nowDateTime);
        OrderRes orderRes3 = orderService.createOrder(orderCreateReq, LocalDateTime.of(2023, 10, 17, 23, 59, 59));
        OrderRes orderRes4 = orderService.createOrder(orderCreateReq, LocalDateTime.of(2023, 10, 18, 22, 31));

        orderRepository.updateOrderStatusCompleted(orderRes1.getId());
        orderRepository.updateOrderStatusCompleted(orderRes2.getId());
        orderRepository.updateOrderStatusCompleted(orderRes3.getId());
        orderRepository.updateOrderStatusCompleted(orderRes4.getId());

        Mockito.when(mailClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);

        boolean result = orderStatisticsService.sendOrderStatisticsMail(nowDateTime.toLocalDate(), "salgu@salgu.com");
        Assertions.assertThat(result).isTrue();

        List<MailSendHistory> mailSendHistories = mailSendHistoryRepository.findAll();
        Assertions.assertThat(mailSendHistories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 12000원입니다.");
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