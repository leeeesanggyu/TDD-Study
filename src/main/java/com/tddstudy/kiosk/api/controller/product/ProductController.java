package com.tddstudy.kiosk.api.controller.product;

import com.tddstudy.kiosk.api.response.ApiResponse;
import com.tddstudy.kiosk.api.service.product.ProductService;
import com.tddstudy.kiosk.api.controller.product.req.ProductCreateReq;
import com.tddstudy.kiosk.api.service.product.res.ProductRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/v1/products/selling")
    public ApiResponse<List<ProductRes>> getSellingProducts() {
        return ApiResponse.ok(productService.getSellingProducts());
    }

    @PostMapping("/api/v1/products/new")
    public ApiResponse<ProductRes> createProduct(@Valid @RequestBody ProductCreateReq productCreateReq) {
        return ApiResponse.ok(productService.createProduct(productCreateReq));
    }
}
