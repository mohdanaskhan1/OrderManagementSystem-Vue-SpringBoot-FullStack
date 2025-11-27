package com.example.springboot3.payload;

import com.example.springboot3.utils.DeliveryType;
import com.example.springboot3.utils.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrdersPatchDTO {
    private Long id;

    @NotBlank(message = "Delivery type is required")
    @Size(max = 50, message = "Delivery type must be less than 50 characters")
    private DeliveryType deliveryType;

    @NotBlank(message = "Status is required")
    @Size(max = 20, message = "Status must be less than 20 characters")
    private Status status;

    @NotNull(message = "Delivery Time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deliveryTime;
}
