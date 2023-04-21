package com.example.carrental.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "car")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @SequenceGenerator(
            name = "car_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "car_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;

    @Column(length = 200)
    private String brand;

    @Column(length = 200)
    private String model;

    @Column(length = 50)
    private String color;

    @Enumerated(EnumType.STRING)
    private CarType carType;

    private BigDecimal price;

}