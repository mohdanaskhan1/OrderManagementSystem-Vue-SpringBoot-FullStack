package com.example.springboot3.payload;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;
}
