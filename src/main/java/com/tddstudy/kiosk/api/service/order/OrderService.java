package com.tddstudy.kiosk.api.service.order;

import com.tddstudy.kiosk.api.controller.order.req.OrderCreateReq;
import com.tddstudy.kiosk.api.service.order.res.OrderRes;
import com.tddstudy.kiosk.domain.order.Order;
import com.tddstudy.kiosk.domain.order.OrderRepository;
import com.tddstudy.kiosk.domain.product.Product;
import com.tddstudy.kiosk.domain.product.ProductRepository;
import com.tddstudy.kiosk.domain.product.ProductType;
import com.tddstudy.kiosk.domain.stock.Stock;
import com.tddstudy.kiosk.domain.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    @Transactional
    public OrderRes createOrder(OrderCreateReq orderCreateReq, LocalDateTime registeredDateTime) {
        List<Product> duplicateProducts = findProductsBy(orderCreateReq);

        deductStock(duplicateProducts);
        Order createdOrder = Order.create(duplicateProducts, registeredDateTime);
        Order savedOrder = orderRepository.save(createdOrder);
        return OrderRes.of(savedOrder);
    }

    private void deductStock(List<Product> products) {
        List<String> stockProductNumbers = extractStockProductNumbers(products);
        Map<String, Stock> stockMap = getProductStockMap(stockProductNumbers);
        Map<String, Long> productQuantityCountingMap = getProductQuantityCountingMap(stockProductNumbers);

        new HashSet<>(stockProductNumbers).forEach(stockProductNumber -> {
                Stock stock = stockMap.get(stockProductNumber);
                int quantity = productQuantityCountingMap.get(stockProductNumber).intValue();

                if (stock.isQuantityLessThan(quantity)) {
                    throw new IllegalArgumentException("재고보다 주문 수량이 더 많습니다.");
                }

                stock.deductionQuantity(quantity);
        });
    }

    /**
     * 재고 차감해야되는 상품 filter
     */
    private List<String> extractStockProductNumbers(List<Product> products) {
        return products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .collect(Collectors.toList());
    }

    /**
     * 재고 엔티티 조회
     */
    private Map<String, Stock> getProductStockMap(List<String> stockProductNumbers) {
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        return stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, stock -> stock));
    }

    /**
     * 상품별 재고 차감 갯수 Counting
     */
    private Map<String, Long> getProductQuantityCountingMap(List<String> stockProductNumbers) {
        return stockProductNumbers.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
    }

    private List<Product> findProductsBy(OrderCreateReq orderCreateReq) {
        List<Product> products = productRepository.findAllByProductNumberIn(orderCreateReq.getProductNumbers());

        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));
        return orderCreateReq.getProductNumbers().stream()
                .map(productMap::get)
                .collect(Collectors.toList());
    }
}
