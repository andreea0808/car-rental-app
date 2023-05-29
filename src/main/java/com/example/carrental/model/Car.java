package com.example.carrental.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "car",
        indexes = @Index(name = "idx_car_type",
                columnList = "car_type")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String brand;

    @Column(length = 50)
    private String model;

    @Column(length = 50)
    private String color;

    @Column(name = "car_type", length = 50)
    @Enumerated(EnumType.STRING)
    private CarType carType;

    private BigDecimal price;

}