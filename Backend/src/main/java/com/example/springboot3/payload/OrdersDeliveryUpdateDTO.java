package com.example.springboot3.payload;

import com.example.springboot3.utils.DeliveryType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrdersDeliveryUpdateDTO {
    @NotNull(message = "Delivery Type is required")
    private DeliveryType deliveryType;

    @NotNull(message = "Delivery Time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deliveryTime;
}
