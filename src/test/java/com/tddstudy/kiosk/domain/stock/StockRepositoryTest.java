package com.tddstudy.kiosk.domain.stock;

import com.tddstudy.kiosk.IntegrationTestSupport;
import com.tddstudy.kiosk.domain.product.Product;
import com.tddstudy.kiosk.domain.product.ProductSellingType;
import com.tddstudy.kiosk.domain.product.ProductType;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StockRepositoryTest extends IntegrationTestSupport {

    @Autowired
    StockRepository stockRepository;

    @DisplayName("상품번호 리스트로 재고를 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        Stock stock1 = Stock.create("001", 1);
        Stock stock2 = Stock.create("002", 2);
        Stock stock3 = Stock.create("003", 3);
        stockRepository.saveAll(List.of(stock1, stock2, stock3));

        List<Stock> stocks = stockRepository.findAllByProductNumberIn(List.of("001", "002"));

        Assertions.assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", 1),
                        Tuple.tuple("002", 2)
                );
    }

    @DisplayName("재고 수량이 주문수량보다 작은지 확인한다.")
    @Test
    void isQuantityLessThan() {
        int quantity = 1;
        Stock stock = Stock.create("001", quantity);

        int orderQuantity = 2;
        boolean result = stock.isQuantityLessThan(orderQuantity);

        Assertions.assertThat(result).isTrue();
    }

    @DisplayName("재고를 수량만큼 차감한다.")
    @Test
    void deductionQuantity() {
        int quantity = 5;
        Stock stock = Stock.create("001", quantity);
        int deductionQuantity = 2;

        int result = stock.deductionQuantity(deductionQuantity);

        Assertions.assertThat(stock.getQuantity()).isEqualTo(3);
        Assertions.assertThat(result).isEqualTo(3);
    }

    @DisplayName("재고보다 많은 수량만큼 차감하면 예외가 발생한다.")
    @Test
    void deductionQuantityException() {
        int quantity = 1;
        Stock stock = Stock.create("001", quantity);
        int deductionQuantity = 2;

        Assertions.assertThatThrownBy(() -> stock.deductionQuantity(deductionQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고보다 많은 수량만큼 차감하여 예외가 발생했습니다.");
    }
}