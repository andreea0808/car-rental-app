package com.example.carrental.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "car",
        indexes = @Index(name = "car_type_idx", columnList = "UPPER(carType")) // can't remember if upper works here
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
