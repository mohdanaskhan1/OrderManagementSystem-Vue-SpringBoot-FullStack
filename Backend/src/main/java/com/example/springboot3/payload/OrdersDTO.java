package com.example.springboot3.payload;

import com.example.springboot3.entity.Orders;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

@Data
public class OrdersDTO{
    @Size(max = 100, message = "Customer name must be less than 100 characters")
    @NotBlank(message = "Customer name is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Customer name must contain only letters and spaces")
    private String customerName;

    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Order Date is required")
    private Date orderDate;

    @NotBlank(message = "Delivery type is required")
    @Size(max = 50, message = "Delivery type must be less than 50 characters")
    private String deliveryType;

    @NotBlank(message = "Status is required")
    @Size(max = 20, message = "Status must be less than 20 characters")
    private String status;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;

    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "HH:MM:SS")
    private Time deliveryTime;
}
