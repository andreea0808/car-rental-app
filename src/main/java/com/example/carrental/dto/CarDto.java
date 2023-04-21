package com.example.carrental.dto;

import com.example.carrental.model.CarType;
import com.example.carrental.validation.ValidCarType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@ValidCarType(message = "Invalid price for car type {carType}")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDto {

    private Long id;
    @NotBlank(message = "BRAND must not be blank")
    private String brand;
    @NotBlank(message = "MODEL must not be blank")
    private String model;
    @NotBlank(message = "COLOR must not be blank")
    private String color;
    @NotNull(message = "CAR TYPE must not be blank")
    private CarType carType;
    @Positive
    @NotNull(message = "PRICE must not be NULL")
    private BigDecimal price;
}
