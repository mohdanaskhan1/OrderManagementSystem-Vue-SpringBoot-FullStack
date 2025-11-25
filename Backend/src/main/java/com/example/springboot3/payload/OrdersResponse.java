package com.example.springboot3.payload;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersResponse {

    private List<OrdersDTO> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;


    // Aggregated statistics
    private Long totalOrders;
    private Double totalAmount;
    private Double averageAmount;
}
