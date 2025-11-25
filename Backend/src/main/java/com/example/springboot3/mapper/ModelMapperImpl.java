package com.example.springboot3.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperImpl {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
