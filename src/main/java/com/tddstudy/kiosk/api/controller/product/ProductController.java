package com.tddstudy.kiosk.api.controller.product;

import com.tddstudy.kiosk.api.service.product.ProductService;
import com.tddstudy.kiosk.api.service.product.response.ProductRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/v1/products/selling")
    public List<ProductRes> getSellingProducts() {
        return productService.getSellingProducts();
    }
}
