package com.example.springboot3.utils;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DeliveryType {
    EXPRESS,
    HOME_DELIVERY,
    TATKAL,
    NORMAL;

    @JsonCreator
    public static DeliveryType from(String value) {
        try {
            return DeliveryType.valueOf(value.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Delivery Type value: " + value);
        }
    }
}
