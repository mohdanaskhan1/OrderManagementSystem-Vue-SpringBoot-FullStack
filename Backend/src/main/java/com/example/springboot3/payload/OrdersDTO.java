package com.example.springboot3.payload;

import com.example.springboot3.utils.DeliveryType;
import com.example.springboot3.utils.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class OrdersDTO {
    private Long id;

    @Size(max = 100, message = "Customer name must be less than 100 characters")
    @NotBlank(message = "Customer name is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Customer name must contain only letters and spaces")
    private String customerName;

    @NotNull(message = "Order Date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate orderDate;

    @NotBlank(message = "Delivery type is required")
    @Size(max = 50, message = "Delivery type must be less than 50 characters")
    private DeliveryType deliveryType;

    @NotBlank(message = "Status is required")
    @Size(max = 20, message = "Status must be less than 20 characters")
    private Status status;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;

    @NotNull(message = "Delivery Time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deliveryTime;
}
