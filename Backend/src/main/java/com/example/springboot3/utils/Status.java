package com.example.springboot3.utils;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {

    DELIVERED,
    CANCELLED,
    PROCESSING,
    SHIPPED;

    @JsonCreator
    public static Status fromString(String value){
        return Status.valueOf(value.toUpperCase());
    }
}
