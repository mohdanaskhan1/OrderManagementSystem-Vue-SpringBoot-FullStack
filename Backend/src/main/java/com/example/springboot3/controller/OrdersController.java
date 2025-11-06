package com.example.springboot3.controller;

import com.example.springboot3.payload.OrdersDTO;
import com.example.springboot3.payload.OrdersPatchDTO;
import com.example.springboot3.entity.Orders;
import com.example.springboot3.service.OrderServices;
import com.example.springboot3.exception.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/")
@RestController
@AllArgsConstructor
public class OrdersController {

    private final OrderServices orderServices;

    @Operation(summary = "Fetch all orders", description = "Retrieve a list of all Orders")
    @GetMapping("orders/")
    public ResponseEntity<Object> FetchAllOrders(HttpServletRequest request) {
        List<Orders> orders = orderServices.findAll();
        return ResponseHandler.generateResponse("Orders fetched successfully", HttpStatus.OK, orders, request);
    }

    @Operation(summary = "Fetch order by ID", description = "Retrieve order details using its unique ID")
    @GetMapping("orders/{id}")
    public ResponseEntity<Object> fetchOrderById(@PathVariable Long id, HttpServletRequest request) {
        Orders order = orderServices.findAllById(id);
        return ResponseHandler.generateResponse("Order fetched successfully", HttpStatus.OK, order, request);
    }


    @Operation(summary = "Create a new order", description = "Creates a new order with all fields")
    @PostMapping("orders/")
    public ResponseEntity<Object> createOrder(@Valid @RequestBody OrdersDTO ordersDTO, HttpServletRequest request) {
        Orders order = orderServices.createOrders(ordersDTO);
        return ResponseHandler.generateResponse("Order created successfully", HttpStatus.CREATED, order, request);
    }

    @Operation(summary = "Delete an order", description = "Deletes an order by its unique ID")
    @DeleteMapping("orders/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable Long id, HttpServletRequest request) {
        orderServices.findAllById(id);
        orderServices.deleteOrder(id);
        return ResponseHandler.generateResponse("Order deleted successfully", HttpStatus.OK, null, request);
    }

    @Operation(summary = "Update specific fields of an order", description = "Partially updates an order using the provided fields")
    @PatchMapping("orders/{id}")
    public ResponseEntity<Object> patchOrder(@PathVariable Long id, @Valid @RequestBody OrdersPatchDTO ordersPatchDTO, HttpServletRequest request) {
        Orders updatedOrder = orderServices.updatePatchOrder(id, ordersPatchDTO);
        return ResponseHandler.generateResponse("Order updated successfully", HttpStatus.OK, updatedOrder, request);
    }
}
