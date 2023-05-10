package com.example.carrental.controller;

import com.example.carrental.dto.CarDto;
import com.example.carrental.exceptions.InvalidCarTypeException;
import com.example.carrental.model.CarType;
import com.example.carrental.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CarControllerTest {
    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        CarController carController = new CarController(carService);
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
    }

    @Test
    void fetchCarById_returnsCarDto() throws Exception {
        CarDto carDto = CarDto.builder()
                .id(1L)
                .brand("Honda")
                .model("Civic")
                .carType(CarType.SUV)
                .price(new BigDecimal(100))
                .build();

        when(carService.fetchCarById((1L))).thenReturn(carDto);

        mockMvc.perform(get("/api/v1/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brand").value("Honda"))
                .andExpect(jsonPath("$.model").value("Civic"))
                .andExpect(jsonPath("$.carType").value("SUV"))
                .andExpect(jsonPath("$.price").value(100));

        verify(carService, times(1)).fetchCarById((1L));
    }

    @Test
    void createCar_returnsSavedCar() throws InvalidCarTypeException {
        CarDto carDto = CarDto.builder()
                .brand("Toyota")
                .model("Camry")
                .carType(CarType.STANDARD)
                .price(BigDecimal.valueOf(20000))
                .build();

        CarDto savedCar = CarDto.builder()
                .id(1L)
                .brand(carDto.getBrand())
                .model(carDto.getModel())
                .carType(carDto.getCarType())
                .price(carDto.getPrice())
                .build();

        when(carService.create(carDto)).thenReturn(savedCar);

       String response = carController.createCar(carDto);

        assertEquals(HttpStatus.CREATED, response);
        assertEquals(String.format("The car with id %d has been saved", savedCar.getId()), response);
    }

    @Test
    void replaceCar_returnsUpdatedCar() {
        Long carId = 1L;
        CarDto carDto = CarDto.builder()
                .brand("Toyota")
                .model("Camry")
                .carType(CarType.STANDARD)
                .price(BigDecimal.valueOf(20000))
                .build();

        CarDto updatedCarDto = CarDto.builder()
                .id(carId)
                .brand(carDto.getBrand())
                .model(carDto.getModel())
                .carType(carDto.getCarType())
                .price(BigDecimal.valueOf(25000))
                .build();

        when(carService.replaceCar(carDto, carId)).thenReturn(updatedCarDto);

        ResponseEntity<CarDto> responseEntity = carController.replaceCar(carId, carDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedCarDto, responseEntity.getBody());
    }

    @Test
    void updatePrice_returnsUpdatedCar() {

        Long carId = 1L;
        CarDto carDto = CarDto.builder()
                .price(BigDecimal.valueOf(25000))
                .build();

        CarDto updatedCarDto = CarDto.builder()
                .id(carId)
                .brand("Toyota")
                .model("Camry")
                .carType(CarType.STANDARD)
                .price(carDto.getPrice())
                .build();

        when(carService.updateCarPrice(carId, carDto)).thenReturn(updatedCarDto);

        ResponseEntity<CarDto> responseEntity = carController.updatePrice(carId, carDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    void testCount() {
        Map<CarType, Long> countResult = new HashMap<>();
        countResult.put(CarType.STANDARD, 5L);
        Mockito.when(carService.countTypes()).thenReturn(countResult);

        ResponseEntity<Map<CarType, Long>> response = carController.count();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(countResult, response.getBody());
    }

    @Test
    void testFindAllCars() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CarDto> carDtos = new PageImpl<>(Arrays.asList(
                new CarDto(1L, "Toyota", "Camry", "red", CarType.STANDARD, BigDecimal.valueOf(19)),
                new CarDto(2L, "Honda", "Civic", "blue", CarType.STANDARD, BigDecimal.valueOf(20))
        ));
        Mockito.when(carService.findAllCarsSortedByPrice(pageable)).thenReturn(carDtos);

        ResponseEntity<Page<CarDto>> response = carController.findAllCars(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carDtos, response.getBody());
    }
}