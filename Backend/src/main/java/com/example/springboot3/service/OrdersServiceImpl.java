package com.example.springboot3.service;

import com.example.springboot3.exception.InvalidOrderOperationException;
import com.example.springboot3.exception.ResourceNotFoundException;
import com.example.springboot3.payload.OrdersDTO;
import com.example.springboot3.payload.OrdersDeliveryUpdateDTO;
import com.example.springboot3.payload.OrdersPatchDTO;
import com.example.springboot3.entity.Orders;
import com.example.springboot3.payload.OrdersResponse;
import com.example.springboot3.repository.OrdersRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrderService {

    @Value("${order.default.date-range-days}")
    private int defaultDays;

    @Value("${order.pagination.default-page-size}")
    private int defaultPageSize;

    @Value("${order.cutoff.hours}")
    private int cutoffHours;

    @Value("${order.express.min-amount}")
    private BigDecimal expressMinAmount;


    private final OrdersRepo ordersRepo;

    private final ModelMapper modelMapper;

    @Autowired
    public OrdersServiceImpl(OrdersRepo ordersRepo, ModelMapper modelMapper) {
        this.ordersRepo = ordersRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<OrdersDTO> findAll() {
        List<Orders> listOfOrders = ordersRepo.findAll();
        return listOfOrders.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public OrdersDTO findAllById(Long id) {
        Orders orders = ordersRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        return mapToDTO(orders);
    }

    @Override
    public OrdersDTO createOrders(OrdersDTO  ordersDTO) {
        Orders orders = mapToEntity(ordersDTO);
        return mapToDTO(ordersRepo.save(orders));
    }

    public void deleteOrder(Long id){
        Orders orders = ordersRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        ordersRepo.delete(orders);
    }

    public OrdersPatchDTO updatePatchOrder(Long id, OrdersPatchDTO  ordersPatchDTO) {
        Orders order = ordersRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Order", "id", id));
        if (ordersPatchDTO.getDeliveryTime()!=null){
            order.setDeliveryTime(ordersPatchDTO.getDeliveryTime());
        }
        if (ordersPatchDTO.getDeliveryType()!=null){
            order.setDeliveryType(ordersPatchDTO.getDeliveryType());
        }
        if (ordersPatchDTO.getStatus()!=null){
            order.setStatus(ordersPatchDTO.getStatus());
        }
        return mapToPatchDTO(ordersRepo.save(order));
    }

    @Override
    public OrdersResponse findFilteredOrders(String status, LocalDate fromDate, LocalDate toDate, BigDecimal minAmount, Boolean includeStats, Integer page, Integer size, String sortBy, String sortDir) {

        if (fromDate == null) fromDate = LocalDate.now().minusDays(defaultDays);
        if (toDate == null) toDate = LocalDate.now();
        if (size == null) size = defaultPageSize;
        if (page == null) page = 0;

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Orders> ordersPage = ordersRepo.findOrdersFiltered(status, minAmount, fromDate, toDate, pageable);
        List<OrdersDTO> listOfDTO = ordersPage.getContent().stream().map(this::mapToDTO).toList();
        OrdersResponse ordersResponse = new OrdersResponse();
        ordersResponse.setContent(listOfDTO);
        ordersResponse.setPageNo(ordersPage.getNumber());
        ordersResponse.setPageSize(ordersPage.getSize());
        ordersResponse.setTotalPages(ordersPage.getTotalPages());
        ordersResponse.setTotalElements(ordersPage.getTotalElements());
        ordersResponse.setLast(ordersPage.isLast());

        if (Boolean.TRUE.equals(includeStats)) {
            List<Object[]> statsList = ordersRepo.findOrdersAggregated(status, minAmount, fromDate, toDate);
            if (!statsList.isEmpty()) {
                Object[] stats = statsList.get(0);
                ordersResponse.setTotalOrders(
                        stats[0] != null ? ((Number) stats[0]).longValue() : 0L
                );
                ordersResponse.setTotalAmount(
                        stats[1] != null ? ((Number) stats[1]).doubleValue() : 0.0
                );
                ordersResponse.setAverageAmount(
                        stats[2] != null ? ((Number) stats[2]).doubleValue() : 0.0
                );
            }
        }

        return ordersResponse;
    }

    @Override
    public OrdersDTO updateDeliveryRules(Long id, OrdersDeliveryUpdateDTO ordersDeliveryUpdateDTO) {
        Orders order = ordersRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        if ("Shipped".equalsIgnoreCase(order.getStatus()) ||
                "Delivered".equalsIgnoreCase(order.getStatus())) {
            throw new InvalidOrderOperationException("Delivery details cannot be updated for shipped or delivered orders.");
        }
        LocalDateTime orderDateTime = order.getDeliveryTime();
        LocalDateTime now = LocalDateTime.now();
        long hoursPassed = java.time.Duration.between(orderDateTime, now).toHours();
        if (hoursPassed >= cutoffHours) {
            throw new InvalidOrderOperationException(
                    "Delivery details cannot be updated after " + cutoffHours + " hours of order creation."
            );
        }
        if ("Express".equalsIgnoreCase(ordersDeliveryUpdateDTO.getDeliveryType())) {
            if (order.getTotalAmount().compareTo(expressMinAmount) < 0) {
                throw new InvalidOrderOperationException(
                        "Express delivery requires a minimum order amount of " + expressMinAmount
                );
            }
        }
        if (ordersDeliveryUpdateDTO.getDeliveryType() != null)
            order.setDeliveryType(ordersDeliveryUpdateDTO.getDeliveryType());

        if (ordersDeliveryUpdateDTO.getDeliveryTime() != null) {
            order.setDeliveryTime(ordersDeliveryUpdateDTO.getDeliveryTime());
        }
        return mapToDTO(ordersRepo.save(order));
    }


    private OrdersDTO mapToDTO(Orders order){
        return modelMapper.map(order,OrdersDTO.class);
    }

    private OrdersPatchDTO mapToPatchDTO(Orders order){
        return modelMapper.map(order,OrdersPatchDTO.class);
    }

    private Orders mapToEntity(OrdersDTO ordersDTO){
        return modelMapper.map(ordersDTO,Orders.class);
    }


}
