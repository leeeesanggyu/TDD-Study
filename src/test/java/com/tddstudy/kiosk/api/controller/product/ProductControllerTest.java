package com.tddstudy.kiosk.api.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tddstudy.kiosk.api.service.product.ProductService;
import com.tddstudy.kiosk.api.controller.product.req.ProductCreateReq;
import com.tddstudy.kiosk.api.service.product.res.ProductRes;
import com.tddstudy.kiosk.domain.product.ProductSellingType;
import com.tddstudy.kiosk.domain.product.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = ProductController.class)  // Controller 테스트 전용 어노테이션
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @DisplayName("새로운 상품을 등록한다.")
    @Test
    void createProduct() throws Exception {
        ProductCreateReq productCreateReq = createProductCreateReq();

        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(productCreateReq))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultAction
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @DisplayName("새로운 상품을 등록할때 상품 타입은 필수값이다.")
    @Test
    void createProductWithoutType() throws Exception {
        ProductCreateReq productCreateReq = ProductCreateReq.builder()
                .type(null)
                .sellingType(ProductSellingType.SELLING)
                .name("소보로빵")
                .price(3500)
                .build();

        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(productCreateReq))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultAction
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("판매 상품을 조회한다.")
    @Test
    void getSellingProducts() throws Exception {
        List<ProductRes> productReses = List.of(createProductRes(1L, "001"), createProductRes(2L, "002"));
        when(productService.getSellingProducts())
                .thenReturn(productReses);

        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/products/selling")
//                        .queryParam("name", "value")
        );

        resultAction
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isArray());
    }

    private ProductRes createProductRes(Long id, String productNumber) {
        return ProductRes.builder()
                .id(id)
                .productNumber(productNumber)
                .type(ProductType.BAKERY)
                .sellingType(ProductSellingType.SELLING)
                .name("소보로빵")
                .price(3500)
                .build();
    }


    private ProductCreateReq createProductCreateReq() {
        return ProductCreateReq.builder()
                .type(ProductType.BAKERY)
                .sellingType(ProductSellingType.SELLING)
                .name("소보로빵")
                .price(3500)
                .build();
    }
}