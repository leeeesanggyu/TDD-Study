package com.tddstudy.kiosk.api.service.product;

import com.tddstudy.kiosk.api.controller.product.req.ProductCreateReq;
import com.tddstudy.kiosk.api.service.product.req.ProductCreateServiceReq;
import com.tddstudy.kiosk.api.service.product.res.ProductRes;
import com.tddstudy.kiosk.domain.product.Product;
import com.tddstudy.kiosk.domain.product.ProductRepository;
import com.tddstudy.kiosk.domain.product.ProductSellingType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductRes> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingTypeIn(ProductSellingType.getDisplay());

        return products.stream()
                .map(ProductRes::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductRes createProduct(ProductCreateServiceReq productCreateServiceReq) {
        String productNumber = createNextProductNumber();

        Product product = productCreateServiceReq.of(productNumber);
        Product savedProduct = productRepository.save(product);
        return ProductRes.of(savedProduct);
    }

    private String createNextProductNumber() {
        int latestProductNumber = Integer.parseInt(productRepository.findLatestProductNumber().orElse("000"));
        return String.format("%03d", latestProductNumber + 1);
    }
}
