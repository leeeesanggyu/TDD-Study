package com.tddstudy.kiosk.product;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingTypeIn() {
        Product americano = Product.builder()
                .type(ProductType.HANDMADE)
                .sellingType(ProductSellingType.SELLING)
                .name("아메리카노")
                .price(2000)
                .build();
        Product cafeLatte = Product.builder()
                .type(ProductType.BOTTLE)
                .sellingType(ProductSellingType.HOLD)
                .name("카페라떼")
                .price(3000)
                .build();
        Product soboro = Product.builder()
                .type(ProductType.BAKERY)
                .sellingType(ProductSellingType.STOP_SELLING)
                .name("소보로빵")
                .price(3500)
                .build();
        productRepository.saveAll(List.of(americano, cafeLatte, soboro));

        List<Product> products =
                productRepository.findAllBySellingTypeIn(List.of(ProductSellingType.SELLING, ProductSellingType.HOLD));

        Assertions.assertThat(products).hasSize(2)
                .extracting("id", "sellingType", "name")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(1L, ProductSellingType.SELLING, "아메리카노"),
                        Tuple.tuple(2L, ProductSellingType.HOLD, "카페라떼")
                );
    }
}