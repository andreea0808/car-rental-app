package com.example.carrental.repository;

import com.example.carrental.model.Car;
import com.example.carrental.model.CarType;
import com.example.carrental.util.NumberOfTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @AfterEach
    public void tearDown() {
        carRepository.deleteAll();
    }

    @Test
    void testCountTypes() {
        Car car1 = Car.builder()
                .id(1L)
                .brand("Brand1")
                .model("Model1")
                .price(BigDecimal.valueOf(33.0))
                .carType(CarType.SUV)
                .build();

        Car car2 = Car.builder()
                .id(2L)
                .brand("Brand2")
                .model("Model2")
                .price(BigDecimal.valueOf(18.0))
                .carType(CarType.STANDARD)
                .build();

        Car car3 = Car.builder()
                .id(3L)
                .brand("Brand3")
                .model("Model3")
                .price(BigDecimal.valueOf(15.0))
                .carType(CarType.ECONOMY)
                .build();

        carRepository.save(car1);
        carRepository.save(car2);
        carRepository.save(car3);

        List<NumberOfTypes> types = carRepository.countTypes();
        assertThat(types)
                .isNotEmpty()
                .hasSize(3);
    }

}