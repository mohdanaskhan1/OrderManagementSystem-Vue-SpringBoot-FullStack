package com.example.springboot3.repository;

import com.example.springboot3.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public interface OrdersRepo extends JpaRepository<Orders, Long> {
}
