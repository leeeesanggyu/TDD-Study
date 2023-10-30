package com.tddstudy.kiosk.api.service.product;

import com.tddstudy.kiosk.IntegrationTestSupport;
import com.tddstudy.kiosk.api.controller.product.req.ProductCreateReq;
import com.tddstudy.kiosk.api.service.product.res.ProductRes;
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

import java.util.List;

class ProductServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("신규 상품을 등록한다. 상품번호는 사장 최근 상품의 상품번호에서 1 증가한 값이다.")
    @Test
    void createProduct() {
        final String targetProductNumber = "003";

        Product americano = createProduct("001", ProductType.HANDMADE, ProductSellingType.SELLING, "아메리카노", 2000);
        Product cafeLatte = createProduct("002", ProductType.BOTTLE, ProductSellingType.HOLD, "카페라떼", 3000);
        productRepository.saveAll(List.of(americano, cafeLatte));

        ProductCreateReq productCreateReq = createProductCreateReq();

        ProductRes productRes = productService.createProduct(productCreateReq.toServiceReq());

        Assertions.assertThat(productRes)
                .extracting("productNumber", "type", "sellingType", "name", "price")
                .contains(targetProductNumber, ProductType.BAKERY, ProductSellingType.STOP_SELLING, "소보로빵", 3500);
    }

    @DisplayName("신규 상품을 등록한다. 최근 상품이 존재하지 않다면 001이 된다.")
    @Test
    void createProductFirst() {
        final String targetProductNumber = "001";
        ProductCreateReq productCreateReq = createProductCreateReq();

        ProductRes productRes = productService.createProduct(productCreateReq.toServiceReq());

        Assertions.assertThat(productRes)
                .extracting("productNumber", "type", "sellingType", "name", "price")
                .contains(targetProductNumber, ProductType.BAKERY, ProductSellingType.STOP_SELLING, "소보로빵", 3500);
    }

    private ProductCreateReq createProductCreateReq() {
        return ProductCreateReq.builder()
                .type(ProductType.BAKERY)
                .sellingType(ProductSellingType.STOP_SELLING)
                .name("소보로빵")
                .price(3500)
                .build();
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