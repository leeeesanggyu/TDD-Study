package com.tddstudy.kiosk.domain.order;

import com.tddstudy.kiosk.domain.product.Product;
import com.tddstudy.kiosk.domain.product.ProductSellingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
