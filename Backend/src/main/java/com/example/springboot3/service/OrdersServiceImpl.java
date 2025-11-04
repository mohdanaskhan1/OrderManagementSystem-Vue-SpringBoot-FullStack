package com.example.springboot3.service;

import com.example.springboot3.dto.OrdersDTO;
import com.example.springboot3.dto.OrdersPatchDTO;
import com.example.springboot3.entity.Orders;
import com.example.springboot3.repository.OrdersRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrdersServiceImpl implements OrderServices {

    private OrdersRepo ordersRepo;


    @Override
    public List<Orders> findAll() {
        return ordersRepo.findAll();
    }

    @Override
    public Orders findAllById(Long id) {
        return ordersRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Orders with id "+id+" not found!"));
    }

    @Override
    public Orders createOrders(OrdersDTO  ordersDTO) {
        Orders order = new Orders();
        order.setCustomerName(ordersDTO.getCustomerName());
        order.setOrderDate(ordersDTO.getOrderDate());
        order.setDeliveryType(ordersDTO.getDeliveryType());
        order.setStatus(ordersDTO.getStatus());
        order.setTotalAmount(ordersDTO.getTotalAmount());
        order.setDeliveryTime(ordersDTO.getDeliveryTime());
        return ordersRepo.save(order);
    }

    public void deleteOrder(Long id){
        ordersRepo.deleteById(id);
    }

    public Orders updatePatchOrder(Long id, OrdersPatchDTO  ordersPatchDTO) {
        Orders order = ordersRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Orders not found"));
        if (ordersPatchDTO.getDeliveryTime()!=null){
            order.setDeliveryTime(ordersPatchDTO.getDeliveryTime());
        }
        if (ordersPatchDTO.getDeliveryType()!=null){
            order.setDeliveryType(ordersPatchDTO.getDeliveryType());
        }
        if (ordersPatchDTO.getStatus()!=null){
            order.setStatus(ordersPatchDTO.getStatus());
        }
        return ordersRepo.save(order);
    }



}
