package com.example.springboot3.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;

@Data
public class OrdersPatchDTO {
    private Long id;

    @Size(max = 50, message = "Delivery type must be less than 50 characters")
    private String deliveryType;

    @Size(max = 20, message = "Status must be less than 20 characters")
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deliveryTime;
}
