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
        Product americano = createProduct("001", ProductType.HANDMADE, ProductSellingType.SELLING, "아메리카노", 2000);
        Product cafeLatte = createProduct("002", ProductType.BOTTLE, ProductSellingType.HOLD, "카페라떼", 3000);
        Product soboro = createProduct("003", ProductType.BAKERY, ProductSellingType.STOP_SELLING, "소보로빵", 3500);
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
        Product americano = createProduct("001", ProductType.HANDMADE, ProductSellingType.SELLING, "아메리카노", 2000);
        Product cafeLatte = createProduct("002", ProductType.BOTTLE, ProductSellingType.HOLD, "카페라떼", 3000);
        Product soboro = createProduct("003", ProductType.BAKERY, ProductSellingType.STOP_SELLING, "소보로빵", 3500);
        productRepository.saveAll(List.of(americano, cafeLatte, soboro));

        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        Assertions.assertThat(products).hasSize(2)
                .extracting("productNumber")
                .containsExactlyInAnyOrder("001", "002");
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 조회한다.")
    @Test
    void findLatestProduct() {
        final String targetProductNumber = "003";

        Product americano = createProduct("001", ProductType.HANDMADE, ProductSellingType.SELLING, "아메리카노", 2000);
        Product cafeLatte = createProduct("002", ProductType.BOTTLE, ProductSellingType.HOLD, "카페라떼", 3000);
        Product soboro = createProduct(targetProductNumber, ProductType.BAKERY, ProductSellingType.STOP_SELLING, "소보로빵", 3500);
        productRepository.saveAll(List.of(americano, cafeLatte, soboro));

        String latestProductNumber = productRepository.findLatestProductNumber()
                .orElse("0");

        Assertions.assertThat(latestProductNumber).isEqualTo(targetProductNumber);
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 조회할때 상품이 없는 경우 '000' 을 반환한다.")
    @Test
    void findLatestProductNull() {
        String latestProductNumber = productRepository.findLatestProductNumber()
                .orElse("000");

        Assertions.assertThat(latestProductNumber).isEqualTo("000");
    }

    private Product createProduct(String targetProductNumber, ProductType bakery, ProductSellingType sellingType, String name, int price) {
        return Product.builder()
                .productNumber(targetProductNumber)
                .type(bakery)
                .sellingType(sellingType)
                .name(name)
                .price(price)
                .build();
    }
}