package com.tddstudy.kiosk.api.service.product;

import com.tddstudy.kiosk.api.service.product.response.ProductRes;
import com.tddstudy.kiosk.product.Product;
import com.tddstudy.kiosk.product.ProductRepository;
import com.tddstudy.kiosk.product.ProductSellingType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
}
