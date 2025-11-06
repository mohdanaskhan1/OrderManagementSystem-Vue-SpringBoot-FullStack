package com.example.springboot3.service;


import com.example.springboot3.payload.OrdersDTO;
import com.example.springboot3.payload.OrdersPatchDTO;
import com.example.springboot3.entity.Orders;
import java.util.List;

public interface OrderServices {
    List<Orders> findAll();
    Orders findAllById(Long id);
    Orders createOrders(OrdersDTO ordersDTO);
    void deleteOrder(Long id);
    Orders updatePatchOrder(Long id, OrdersPatchDTO ordersPatchDTO);
}
