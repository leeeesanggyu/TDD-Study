package com.tddstudy.kiosk.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * select *
     * from product
     * where selling_type in ('?', '?');
     */
    List<Product> findAllBySellingTypeIn(List<ProductSellingType> sellingTypes);

    List<Product> findAllByProductNumberIn(List<String> productNumbers);

    @Query(
            value = "SELECT p.product_number FROM product p ORDER BY p.id DESC LIMIT 1",
            nativeQuery = true
    )
    Optional<String> findLatestProductNumber();
}
