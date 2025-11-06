package com.example.springboot3.service;


import com.example.springboot3.payload.OrdersDTO;
import com.example.springboot3.payload.OrdersDeliveryUpdateDTO;
import com.example.springboot3.payload.OrdersPatchDTO;
import com.example.springboot3.payload.OrdersResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<OrdersDTO> findAll();
    OrdersDTO findAllById(Long id);
    OrdersDTO createOrders(OrdersDTO ordersDTO);
    void deleteOrder(Long id);
    OrdersPatchDTO updatePatchOrder(Long id, OrdersPatchDTO ordersPatchDTO);
    OrdersResponse findFilteredOrders(String status,
                                      LocalDate fromDate,
                                      LocalDate toDate,
                                      BigDecimal minAmount,
                                      Boolean includeStats,
                                      Integer page,
                                      Integer size,
                                      String sortBy,
                                      String sortDir
                                       );
    OrdersDTO updateDeliveryRules(Long id, OrdersDeliveryUpdateDTO ordersDeliveryUpdateDTO);
}
