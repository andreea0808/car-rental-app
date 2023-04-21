package com.example.carrental.mapper;

import com.example.carrental.dto.CarDto;
import com.example.carrental.model.Car;
import com.example.carrental.model.CarType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CarMapperTest {

    private static CarMapper carMapper;

    @BeforeAll
    public static void setUp() {
        carMapper = new CarMapperImpl();
    }

    @Test
    void testToDto() {
        Car car = Car.builder()
                .id(1L)
                .brand("Toyota")
                .model("Corolla")
                .color("red")
                .price(BigDecimal.valueOf(19.0))
                .carType(CarType.STANDARD)
                .build();

        CarDto dto = carMapper.toDto(car);

        assertAll(
                () -> {
                    assertThat(dto).isNotNull();
                    assertThat(dto.getId()).isEqualTo(1);
                    assertEquals(car.getId(), dto.getId());
                    assertEquals(car.getModel(), dto.getModel());
                    assertEquals(car.getBrand(), dto.getBrand());
                    assertEquals(car.getId(), dto.getId());
                    assertEquals(car.getPrice(), dto.getPrice());
                    assertEquals(car.getCarType(), dto.getCarType());
                }
        );
    }

    @Test
    void testToDto_withNullCar() {
        Car car = null;

        CarDto carDto = carMapper.toDto(null);

        assertNull(carDto);
    }

    @Test
    void testToEntity() {
        CarDto dto = CarDto.builder()
                .id(1L)
                .brand("Toyota")
                .model("Corolla")
                .color("red")
                .price(BigDecimal.valueOf(19.0))
                .carType(CarType.STANDARD)
                .build();

        Car car = carMapper.toEntity(dto);

        assertThat(car).isNotNull();
        assertThat(car.getId()).isEqualTo(1);
        assertThat(car.getBrand()).isEqualTo("Toyota");
        assertThat(car.getModel()).isEqualTo("Corolla");
        assertThat(car.getColor()).isEqualTo("red");
        assertThat(car.getCarType()).isEqualTo(CarType.STANDARD);
    }

    @Test
    void testToEntity_withNullDto() {
        CarDto carDto = null;

        Car car = carMapper.toEntity(null);

        assertNull(car);
    }

    @Test
    void testToDtoList() {
        List<Car> cars = Arrays.asList(
                new Car(1L, "Toyota", "Corolla", "Red", CarType.STANDARD, BigDecimal.valueOf(19.0)),
                new Car(2L, "Honda", "Civic", "Blue", CarType.STANDARD, BigDecimal.valueOf(20.0))
        );

        List<CarDto> dtos = carMapper.toDtoList(cars);

        assertEquals(cars.size(), dtos.size());
        assertEquals(cars.get(0).getId(), dtos.get(0).getId());
        assertEquals(cars.get(0).getCarType(), dtos.get(0).getCarType());
        assertEquals(cars.get(0).getBrand(), dtos.get(0).getBrand());
        assertEquals(cars.get(1).getId(), dtos.get(1).getId());
        assertEquals(cars.get(1).getId(), dtos.get(1).getId());
        assertEquals(cars.get(1).getCarType(), dtos.get(1).getCarType());
        assertEquals(cars.get(1).getBrand(), dtos.get(1).getBrand());
    }

    @Test
    void testToDtoList_withNullList() {
        List<Car> cars = null;

        List<CarDto> carDtos = carMapper.toDtoList(cars);

        assertNull(carDtos);
    }

    @Test
    void shouldUpdateCarPriceAndIgnoreOtherFields() {
        CarDto carDto = CarDto.builder()
                .id(1L)
                .brand("Toyota")
                .model("Corolla")
                .color("red")
                .carType(CarType.STANDARD)
                .price(BigDecimal.valueOf(25.0))
                .build();

        Car car = Car.builder()
                .id(1L)
                .brand("Mazda")
                .model("x4")
                .color("blue")
                .carType(CarType.STANDARD)
                .price(BigDecimal.valueOf(20.0))
                .build();

        Car carToUpdate = carMapper.updateCar(carDto, car);

        assertThat(carToUpdate.getBrand()).isEqualTo("Mazda");
        assertThat(carToUpdate.getModel()).isEqualTo("x4");
        assertThat(carToUpdate.getColor()).isEqualTo("blue");
        assertThat(carToUpdate.getCarType()).isEqualTo(CarType.STANDARD);
        assertThat(carToUpdate.getPrice()).isEqualTo(BigDecimal.valueOf(25.0));
    }

    @Test
    void testUpdateCar_withDtoAndCar_returnsUpdatedCar() {
        CarDto dto = new CarDto();
        dto.setPrice(BigDecimal.valueOf(27.0));

        Car car = Car.builder()
                .id(1L)
                .brand("Toyota")
                .model("Corolla")
                .color("red")
                .carType(CarType.STANDARD)
                .price(BigDecimal.valueOf(20.0))
                .build();

        Car updatedCar = carMapper.updateCar(dto, car);

        assertNotNull(updatedCar);
        assertEquals(car.getId(), updatedCar.getId());
        assertEquals(car.getBrand(), updatedCar.getBrand());
        assertEquals(car.getModel(), updatedCar.getModel());
        assertEquals(car.getColor(), updatedCar.getColor());
        assertEquals(car.getCarType(), updatedCar.getCarType());
        assertEquals(dto.getPrice(), updatedCar.getPrice());
    }
}