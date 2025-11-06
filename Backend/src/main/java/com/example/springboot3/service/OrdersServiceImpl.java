package com.example.springboot3.service;

import com.example.springboot3.exception.ResourceNotFoundException;
import com.example.springboot3.payload.OrdersDTO;
import com.example.springboot3.payload.OrdersPatchDTO;
import com.example.springboot3.entity.Orders;
import com.example.springboot3.repository.OrdersRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrderServices {

    private final OrdersRepo ordersRepo;

    private ModelMapper modelMapper;

    public OrdersServiceImpl(OrdersRepo ordersRepo, ModelMapper modelMapper) {
        this.ordersRepo = ordersRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Orders> findAll() {
        return ordersRepo.findAll();
    }

    @Override
    public Orders findAllById(Long id) {
        return ordersRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Orders with id "+id+" not found!"));
    }

    @Override
    public Orders createOrders(OrdersDTO  ordersDTO) {
//        Orders order = new Orders();
//        order.setCustomerName(ordersDTO.getCustomerName());
//        order.setOrderDate(ordersDTO.getOrderDate());
//        order.setDeliveryType(ordersDTO.getDeliveryType());
//        order.setStatus(ordersDTO.getStatus());
//        order.setTotalAmount(ordersDTO.getTotalAmount());
//        order.setDeliveryTime(ordersDTO.getDeliveryTime());
        Orders orders = DTOToMap(ordersDTO);
        return ordersRepo.save(orders);
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

    //ENTITY TO DTO
//    private OrdersDTO mapToDTO(Orders order){
//        OrdersDTO ordersDTO = new OrdersDTO();
//        ordersDTO.setCustomerName(order.getCustomerName());
//        ordersDTO.setOrderDate(order.getOrderDate());
//        ordersDTO.setDeliveryType(order.getDeliveryType());
//        ordersDTO.setStatus(order.getStatus());
//        ordersDTO.setTotalAmount(order.getTotalAmount());
//        ordersDTO.setDeliveryTime(order.getDeliveryTime());
//        return ordersDTO;
//    }

    private OrdersDTO mapToDTO(Orders order){
        return modelMapper.map(order,OrdersDTO.class);
    }

//    //DTO TO ENTITY
//    private Orders mapToEntity(OrdersDTO ordersDTO){
//        Orders orders = new Orders();
//        orders.setCustomerName(ordersDTO.getCustomerName());
//        orders.setOrderDate(ordersDTO.getOrderDate());
//        orders.setDeliveryType(ordersDTO.getDeliveryType());
//        orders.setStatus(ordersDTO.getStatus());
//        orders.setTotalAmount(ordersDTO.getTotalAmount());
//        orders.setDeliveryTime(ordersDTO.getDeliveryTime());
//        return orders;
//    }

    private Orders DTOToMap(OrdersDTO ordersDTO){
        return modelMapper.map(ordersDTO,Orders.class);
    }


}
