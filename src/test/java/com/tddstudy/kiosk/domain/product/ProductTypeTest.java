package com.tddstudy.kiosk.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class ProductTypeTest {

    @DisplayName("상품 타입이 재고를 사용하는 타입인지 확인한다.")
    @Test
    void containsStockType() {
        boolean result1 = ProductType.containsStockType(ProductType.HANDMADE);
        boolean result2 = ProductType.containsStockType(ProductType.BOTTLE);
        boolean result3 = ProductType.containsStockType(ProductType.BAKERY);

        Assertions.assertThat(result1).isFalse();
        Assertions.assertThat(result2).isTrue();
        Assertions.assertThat(result3).isTrue();
    }
}