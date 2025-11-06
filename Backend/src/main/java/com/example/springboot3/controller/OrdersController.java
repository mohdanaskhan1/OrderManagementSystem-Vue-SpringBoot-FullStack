package com.example.springboot3.controller;

import com.example.springboot3.payload.OrdersDTO;
import com.example.springboot3.payload.OrdersDeliveryUpdateDTO;
import com.example.springboot3.payload.OrdersPatchDTO;
import com.example.springboot3.payload.OrdersResponse;
import com.example.springboot3.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/orders")
@RestController
public class OrdersController {

    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            summary = "Fetch all orders",
            description = "Retrieve a list of all Orders"
    )
    @GetMapping("/fetch")
    public ResponseEntity<List<OrdersDTO>> FetchAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @Operation(
            summary = "Fetch order by ID",
            description = "Retrieve order details using its unique ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<OrdersDTO> fetchOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findAllById(id));
    }


    @Operation(
            summary = "Create a new order",
            description = "Creates a new order with all fields"
    )
    @PostMapping
    public ResponseEntity<OrdersDTO> createOrder(@Valid @RequestBody OrdersDTO ordersDTO) {
        return new ResponseEntity<>(orderService.createOrders(ordersDTO), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete an order",
            description = "Deletes an order by its unique ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }

    @Operation(
            summary = "Update specific fields of an order",
            description = "Partially updates an order using the provided fields"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<OrdersPatchDTO> patchOrder(@PathVariable Long id, @Valid @RequestBody OrdersPatchDTO ordersPatchDTO) {
        return ResponseEntity.ok(orderService.updatePatchOrder(id, ordersPatchDTO));
    }

    @Operation(
            summary = "Retrieve filtered orders",
            description = "Fetch orders based on optional filters such as status, date range, minimum amount, pagination, and sorting."
    )
    @GetMapping
    public ResponseEntity<OrdersResponse> findFilteredOrders(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(value = "minAmount", required = false) BigDecimal minAmount,
            @RequestParam(required = false) Boolean includeStats,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sortBy", defaultValue = "orderDate", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir
            ){
        return ResponseEntity.ok(orderService.findFilteredOrders(status, fromDate, toDate, minAmount, includeStats, page, size, sortBy, sortDir ));
    }

    @Operation(
            summary = "Update delivery rules",
            description = "Updates delivery type & delivery time with business rules"
    )
    @PatchMapping("/{id}/delivery")
    public ResponseEntity<OrdersDTO> updateDeliveryRules(@PathVariable Long id,
                                                                       @Valid @RequestBody OrdersDeliveryUpdateDTO ordersDeliveryUpdateDTO){
        return ResponseEntity.ok(orderService.updateDeliveryRules(id, ordersDeliveryUpdateDTO));
    }

}
