package com.tddstudy.kiosk.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * select *
     * from product
     * where selling_type in ('?', '?');
     */
    List<Product> findAllBySellingTypeIn(List<ProductSellingType> sellingTypes);
}