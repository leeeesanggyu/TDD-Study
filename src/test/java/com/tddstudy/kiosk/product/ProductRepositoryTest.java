package com.tddstudy.kiosk.product;

import com.tddstudy.kiosk.domain.product.Product;
import com.tddstudy.kiosk.domain.product.ProductRepository;
import com.tddstudy.kiosk.domain.product.ProductSellingType;
import com.tddstudy.kiosk.domain.product.ProductType;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingTypeIn() {
        Product americano = Product.builder()
                .productNumber("001")
                .type(ProductType.HANDMADE)
                .sellingType(ProductSellingType.SELLING)
                .name("아메리카노")
                .price(2000)
                .build();
        Product cafeLatte = Product.builder()
                .productNumber("002")
                .type(ProductType.BOTTLE)
                .sellingType(ProductSellingType.HOLD)
                .name("카페라떼")
                .price(3000)
                .build();
        Product soboro = Product.builder()
                .productNumber("003")
                .type(ProductType.BAKERY)
                .sellingType(ProductSellingType.STOP_SELLING)
                .name("소보로빵")
                .price(3500)
                .build();
        productRepository.saveAll(List.of(americano, cafeLatte, soboro));

        List<Product> products =
                productRepository.findAllBySellingTypeIn(List.of(ProductSellingType.SELLING, ProductSellingType.HOLD));

        Assertions.assertThat(products).hasSize(2)
                .extracting("productNumber", "sellingType", "name")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", ProductSellingType.SELLING, "아메리카노"),
                        Tuple.tuple("002", ProductSellingType.HOLD, "카페라떼")
                );
    }

    @DisplayName("상품번호 리스트로 상품을 조회한다.")
    @Test
    void findAllByIdIn() {
        Product americano = Product.builder()
                .productNumber("001")
                .type(ProductType.HANDMADE)
                .sellingType(ProductSellingType.SELLING)
                .name("아메리카노")
                .price(2000)
                .build();
        Product cafeLatte = Product.builder()
                .productNumber("002")
                .type(ProductType.BOTTLE)
                .sellingType(ProductSellingType.HOLD)
                .name("카페라떼")
                .price(3000)
                .build();
        Product soboro = Product.builder()
                .productNumber("003")
                .type(ProductType.BAKERY)
                .sellingType(ProductSellingType.STOP_SELLING)
                .name("소보로빵")
                .price(3500)
                .build();
        productRepository.saveAll(List.of(americano, cafeLatte, soboro));

        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        Assertions.assertThat(products).hasSize(2)
                .extracting("productNumber")
                .containsExactlyInAnyOrder("001", "002");
    }
}