package com.example.springboot3.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrdersDeliveryUpdateDTO {
    @Size(max = 50, message = "Delivery type must be less than 50 characters")
    private String deliveryType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deliveryTime;
}
