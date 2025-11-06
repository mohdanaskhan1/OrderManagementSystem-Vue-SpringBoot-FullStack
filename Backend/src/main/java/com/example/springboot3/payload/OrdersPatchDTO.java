package com.example.springboot3.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Time;

@Data
public class OrdersPatchDTO {
    @Size(max = 50, message = "Delivery type must be less than 50 characters")
    private String deliveryType;

    @Size(max = 20, message = "Status must be less than 20 characters")
    private String status;

    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "HH:MM:SS")
    private Time deliveryTime;
}
