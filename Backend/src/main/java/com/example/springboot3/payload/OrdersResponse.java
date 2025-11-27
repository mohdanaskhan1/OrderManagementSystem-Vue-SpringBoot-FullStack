package com.example.springboot3.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
