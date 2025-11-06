package com.example.springboot3.repository;

import com.example.springboot3.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface OrdersRepo extends JpaRepository<Orders, Long> {

    @Query("""
        SELECT o FROM Orders o
        WHERE (:status IS NULL OR o.status = :status)
        AND (:minAmount IS NULL OR o.totalAmount >= :minAmount)
        AND (:fromDate IS NULL OR o.orderDate >= :fromDate)
        AND (:toDate IS NULL OR o.orderDate <= :toDate)
    """)
    Page<Orders> findOrdersFiltered(
            @Param("status") String status,
            @Param("minAmount") BigDecimal minAmount,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable
    );




    @Query("""
    SELECT COUNT(o), COALESCE(SUM(o.totalAmount), 0), COALESCE(AVG(o.totalAmount), 0)
    FROM Orders o
    WHERE (:status IS NULL OR o.status = :status)
    AND (:minAmount IS NULL OR o.totalAmount >= :minAmount)
    AND (:fromDate IS NULL OR o.orderDate >= :fromDate)
    AND (:toDate IS NULL OR o.orderDate <= :toDate)
    """)
    List<Object[]> findOrdersAggregated(
            @Param("status") String status,
            @Param("minAmount") BigDecimal minAmount,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
}
