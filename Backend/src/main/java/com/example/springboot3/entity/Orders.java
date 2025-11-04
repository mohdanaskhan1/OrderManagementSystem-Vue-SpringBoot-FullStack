package com.example.springboot3.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", length = 100, nullable = false)
    private String customerName;

    @Column(name = "order_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @Column(name = "delivery_type", length = 50, nullable = false)
    private String deliveryType;

    @Column(name = "status" , length = 20, nullable = false)
    private String status;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "delivery_time" , nullable = true)
    @DateTimeFormat(pattern = "HH:MM:SS")
    @Temporal(TemporalType.TIME)
    private Time deliveryTime;
}
