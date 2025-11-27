package com.example.springboot3.payload;

import com.example.springboot3.utils.DeliveryType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrdersDeliveryUpdateDTO {
    @NotBlank(message = "Delivery type is required")
    @Size(max = 50, message = "Delivery type must be less than 50 characters")
    private DeliveryType deliveryType;

    @NotNull(message = "Delivery Time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deliveryTime;
}
