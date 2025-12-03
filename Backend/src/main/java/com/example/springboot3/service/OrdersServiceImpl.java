package com.example.springboot3.service;

import com.example.springboot3.entity.Orders;
import com.example.springboot3.exception.InvalidOrderOperationException;
import com.example.springboot3.exception.ResourceNotFoundException;
import com.example.springboot3.payload.OrdersDTO;
import com.example.springboot3.payload.OrdersDeliveryUpdateDTO;
import com.example.springboot3.payload.OrdersPatchDTO;
import com.example.springboot3.payload.OrdersResponse;
import com.example.springboot3.repository.OrdersRepo;
import com.example.springboot3.utils.DeliveryType;
import com.example.springboot3.utils.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
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
    public OrdersDTO createOrders(OrdersDTO ordersDTO) {
        Orders orders = mapToEntity(ordersDTO);
        return mapToDTO(ordersRepo.save(orders));
    }

    public void deleteOrder(Long id) {
        Orders orders = ordersRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        ordersRepo.delete(orders);
    }

    public OrdersPatchDTO updatePatchOrder(Long id, OrdersPatchDTO ordersPatchDTO) {
        Orders order = ordersRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        if (ordersPatchDTO.getDeliveryTime() != null) {
            order.setDeliveryTime(ordersPatchDTO.getDeliveryTime());
        }
        if (ordersPatchDTO.getDeliveryType() != null) {
            order.setDeliveryType(ordersPatchDTO.getDeliveryType());
        }
        if (ordersPatchDTO.getStatus() != null) {
            order.setStatus(ordersPatchDTO.getStatus());
        }
        return mapToPatchDTO(ordersRepo.save(order));
    }

    @Override
    public OrdersResponse findFilteredOrders(Status status, LocalDate fromDate, LocalDate toDate, BigDecimal minAmount, Boolean includeStats, Pageable pageable) {

        if (fromDate == null) fromDate = LocalDate.now().minusDays(defaultDays);
        if (toDate == null) toDate = LocalDate.now();

        if (pageable.getPageSize() == 10 || pageable.getPageSize() == 0) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    defaultPageSize,
                    pageable.getSort()
            );
        }

        Page<Orders> ordersPage = ordersRepo.findOrdersFiltered(status, minAmount, fromDate, toDate, pageable);
        OrdersResponse ordersResponse = mapToOrdersResponse(ordersPage);

        if (Boolean.TRUE.equals(includeStats)) {
            List<Object[]> statsList = ordersRepo.findOrdersAggregated(status, minAmount, fromDate, toDate);
            if (!statsList.isEmpty()) {
                Object[] stats = statsList.getFirst();
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
        if (Status.SHIPPED.equals(order.getStatus()) ||
                Status.DELIVERED.equals(order.getStatus())) {
            throw new InvalidOrderOperationException("Delivery details cannot be updated for shipped or delivered orders.");
        }
        if (DeliveryType.EXPRESS.equals(ordersDeliveryUpdateDTO.getDeliveryType())) {
            if (order.getTotalAmount().compareTo(expressMinAmount) < 0) {
                throw new InvalidOrderOperationException(
                        "Express delivery requires a minimum order amount of " + expressMinAmount
                );
            }
        }
        LocalDateTime currentDeliveryTime = order.getDeliveryTime();
        System.out.println(currentDeliveryTime);
        if (currentDeliveryTime == null) {
            throw new InvalidOrderOperationException("Delivery time is not set. Cannot validate cutoff.");
        }
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        long hoursPassed = Duration.between(currentDeliveryTime, now).toHours();
        System.out.println(hoursPassed);
        if (hoursPassed <= cutoffHours) {
            throw new InvalidOrderOperationException(
                    "Delivery details cannot be updated before " + cutoffHours + " hours from the delivery time."
            );
        }
        if (ordersDeliveryUpdateDTO.getDeliveryType() != null)
            order.setDeliveryType(ordersDeliveryUpdateDTO.getDeliveryType());
        if (ordersDeliveryUpdateDTO.getDeliveryTime() != null) {
            order.setDeliveryTime(ordersDeliveryUpdateDTO.getDeliveryTime());
        }
        return mapToDTO(ordersRepo.save(order));
    }

    @Override
    public List<OrdersDTO> createBulkOrders(List<OrdersDTO> ordersDTOList) {
        List<Orders> ordersEntities = ordersDTOList.stream().map(this::mapToEntity).toList();
        List<Orders> ordersList = ordersRepo.saveAll(ordersEntities);
        return ordersList.stream().map(this::mapToDTO).toList();
    }


    private OrdersDTO mapToDTO(Orders order) {
        return modelMapper.map(order, OrdersDTO.class);
    }

    private OrdersPatchDTO mapToPatchDTO(Orders order) {
        return modelMapper.map(order, OrdersPatchDTO.class);
    }

    private OrdersResponse mapToOrdersResponse(Page<Orders> ordersPage) {

        OrdersResponse response = modelMapper.map(ordersPage, OrdersResponse.class);
        List<OrdersDTO> dtoList = ordersPage.getContent()
                .stream()
                .map(this::mapToDTO)
                .toList();
        response.setContent(dtoList);
        return response;
    }

    private Orders mapToEntity(OrdersDTO ordersDTO) {
        return modelMapper.map(ordersDTO, Orders.class);
    }


}
