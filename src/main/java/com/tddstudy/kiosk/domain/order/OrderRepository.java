package com.tddstudy.kiosk.domain.order;

import com.tddstudy.kiosk.domain.product.Product;
import com.tddstudy.kiosk.domain.product.ProductSellingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o " +
            "WHERE o.registeredDateTime >= :startDateTime " +
            "AND o.registeredDateTime < :endDateTime " +
            "AND o.orderStatus = :orderStatus")
    List<Order> findOrdersBy(LocalDateTime startDateTime, LocalDateTime endDateTime, OrderStatus orderStatus);

    @Transactional
    @Modifying
    @Query("UPDATE Order SET orderStatus = 'PAYMENT_COMPLETED' WHERE id = :id")
    int updateOrderStatusCompleted(Long id);
}
