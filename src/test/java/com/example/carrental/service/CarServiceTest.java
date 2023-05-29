package com.example.carrental.service;

import com.example.carrental.dto.CarDto;
import com.example.carrental.exceptions.CarNotFoundException;
import com.example.carrental.exceptions.InvalidCarTypeException;
import com.example.carrental.mapper.CarMapper;
import com.example.carrental.model.Car;
import com.example.carrental.model.CarType;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.util.NumberOfTypes;
import com.example.carrental.validation.CarTypeValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.BDDAssumptions.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CarService.class})
@ExtendWith(SpringExtension.class)
class CarServiceTest {

    private static final Long EXISTING_CAR_ID = 1L;
    private static final Long NON_EXISTING_CAR_ID = 2L;
    private static final String CAR_NOT_FOUND = "Car with id {0} not found.";

    @MockBean
    private CarMapper carMapper;
    @MockBean
    private CarRepository carRepository;
    @MockBean
    private CarTypeValidator carTypeValidator;

    @Autowired
    private CarService carService;

    @Test
    void shouldCreateCar() throws InvalidCarTypeException {
        CarDto carDto = new CarDto(1L, "Toyota", "Corola", "red", CarType.SUV, BigDecimal.valueOf(35));
        Car car = new Car(1L, "Toyota", "Corola", "red", CarType.SUV, BigDecimal.valueOf(35));
        when(carMapper.toEntity(carDto)).thenReturn(car);
        when(carMapper.toDto(car)).thenReturn(carDto);
        when(carTypeValidator.isValid(carDto, null)).thenReturn(true);

        CarDto result = carService.create(carDto);

        assertThat(result).isEqualTo(carDto);
    }

    @Test
    void shouldThrowInvalidCarTypeExceptionWhenCreatingInvalidCarType() throws InvalidCarTypeException {
        CarDto carDto = new CarDto(1L, "Toyota", "Corola", "red", CarType.STANDARD, BigDecimal.valueOf(100));
        when(carTypeValidator.isValid(carDto, null)).thenReturn(false);

        assertThatThrownBy(() -> carService.create(carDto)).isInstanceOf(InvalidCarTypeException.class);
    }

    @Test
    void shouldReplaceCar() throws CarNotFoundException, InvalidCarTypeException {
        CarDto carDto = new CarDto(1L, "Toyota", "Corola", "red", CarType.SUV, BigDecimal.valueOf(35));
        Car car = new Car(1L, "Toyota", "Yaris", "red", CarType.STANDARD, BigDecimal.valueOf(22));
        when(carMapper.toEntity(carDto)).thenReturn(car);
        when(carMapper.toDto(car)).thenReturn(carDto);
        when(carRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(car));
        when(carTypeValidator.isValid(carDto, null)).thenReturn(true);

        CarDto result = carService.replaceCar(carDto, 1L);

        assertThat(result).isEqualTo(carDto);
        assertThat(car.getPrice()).isEqualTo(BigDecimal.valueOf(35));
    }

    @Test
    void shouldThrowCarNotFoundExceptionWhenReplacingNonexistentCar() throws CarNotFoundException, InvalidCarTypeException {
        CarDto carDto = new CarDto(1L, "Toyota", "Corola", "red", CarType.SUV, BigDecimal.valueOf(35));
        Long id = 2L;
        given(carRepository.findById(id)).isNotPresent(); // Simulate the car not being found in the database

        assertThrows(CarNotFoundException.class, () -> carService.replaceCar(carDto, id));
    }


    @Test
    void shouldUpdateCarPriceSuccessfully() throws CarNotFoundException, InvalidCarTypeException {
        Long existingCarId = 1L;
        Car existingCar = new Car();
        existingCar.setId(existingCarId);
        CarDto existingCarDto = new CarDto();
        existingCarDto.setId(existingCarId);
        CarDto updatedCarDto = new CarDto();
        updatedCarDto.setId(existingCarId);

        Mockito.when(carMapper.toEntity(existingCarDto)).thenReturn(existingCar);
        Mockito.when(carRepository.findById(existingCarId)).thenReturn(Optional.of(existingCar));

        Mockito.when(carTypeValidator.isValid(updatedCarDto, null)).thenReturn(true);
        Mockito.when(carMapper.toDto(existingCar)).thenReturn(existingCarDto);
        Mockito.when(carMapper.updateCar(updatedCarDto, existingCar)).thenReturn(existingCar);
        Mockito.when(carRepository.save(existingCar)).thenReturn(existingCar);

        CarDto result = carService.updateCarPrice(existingCarId, updatedCarDto);

        assertEquals(existingCarDto, result);
        Mockito.verify(carMapper).toEntity(updatedCarDto);
        Mockito.verify(carMapper).updateCar(updatedCarDto, existingCar);
        Mockito.verify(carRepository).save(existingCar);
        Mockito.verify(carMapper).toDto(existingCar);
    }

    @Test
    void testFindAllCarsSortedByPrice() {
        Pageable pageable = PageRequest.of(0, 10);

        Car car1 = new Car(1L, "Toyota", "Camry", "red", CarType.STANDARD, BigDecimal.valueOf(25000L));
        Car car2 = new Car(2L, "Honda", "Civic", "white", CarType.ECONOMY, BigDecimal.valueOf(18000L));
        Car car3 = new Car(3L, "Jeep", "Wrangler", "black", CarType.SUV, BigDecimal.valueOf(35000L));

        Page<Car> cars = new PageImpl<>(Arrays.asList(car2, car1, car3), pageable, 3L);

        when(carRepository.findAllByOrderByPriceAsc(pageable)).thenReturn(cars);

        CarDto carDto1 = new CarDto(1L, "Toyota", "Camry", "red", CarType.STANDARD, BigDecimal.valueOf(25000L));
        CarDto carDto2 = new CarDto(2L, "Honda", "Civic", "white", CarType.ECONOMY, BigDecimal.valueOf(18000L));
        CarDto carDto3 = new CarDto(3L, "Jeep", "Wrangler", "black", CarType.SUV, BigDecimal.valueOf(35000L));

        when(carMapper.toDto(car1)).thenReturn(carDto1);
        when(carMapper.toDto(car2)).thenReturn(carDto2);
        when(carMapper.toDto(car3)).thenReturn(carDto3);

        List<CarDto> expected = Arrays.asList(carDto2, carDto1, carDto3);

        Page<CarDto> result = carService.findAllCarsSortedByPrice(pageable);

        assertEquals(expected, result.getContent());
    }

    @Test
    void testCountTypesReturnsExpectedMap() {
        List<NumberOfTypes> numberOfTypesList = Arrays.asList(
                new NumberOfTypes() {
                    @Override
                    public CarType getType() {
                        return CarType.ECONOMY;
                    }

                    @Override
                    public Long getNr() {
                        return 2L;
                    }
                },
                new NumberOfTypes() {
                    @Override
                    public CarType getType() {
                        return CarType.STANDARD;
                    }

                    @Override
                    public Long getNr() {
                        return 3L;
                    }
                },
                new NumberOfTypes() {
                    @Override
                    public CarType getType() {
                        return CarType.SUV;
                    }

                    @Override
                    public Long getNr() {
                        return 1L;
                    }
                }
        );
        when(carRepository.countTypes()).thenReturn(numberOfTypesList);

        // Call the countTypes() method and verify the returned map
        Map<CarType, Long> result = carService.countTypes();
        assertEquals(3, result.size());
        assertEquals(2L, result.get(CarType.ECONOMY));
        assertEquals(3L, result.get(CarType.STANDARD));
        assertEquals(1L, result.get(CarType.SUV));
    }

    @Test
    void shouldReturnEmptyMapWhenNoCarsFound() {
        List<NumberOfTypes> carTypeCounts = Collections.emptyList();
        assertThat(carRepository.countTypes()).isEqualTo(carTypeCounts);

        Map<CarType, Long> result = carService.countTypes();

        assertTrue(result.isEmpty());
    }

    @Test
    void testFetchCarByIdReturnsCarDtoForExistingCar() {
        Long carId = 1L;
        Car existingCar = new Car();
        existingCar.setId(carId);
        existingCar.setBrand("Toyota");
        existingCar.setModel("Corolla");
        existingCar.setPrice(BigDecimal.valueOf(25.0));

        CarDto carDto = new CarDto();
        carDto.setId(EXISTING_CAR_ID);
        carDto.setCarType(CarType.ECONOMY);
        carDto.setBrand("Toyota");
        carDto.setModel("Corolla");
        carDto.setPrice(BigDecimal.valueOf(25.0));

        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        when(carMapper.toDto(existingCar)).thenReturn(carDto);

        CarDto result = carService.fetchCarById(carId);

        assertNotNull(result);
        assertEquals(carId, result.getId());
        assertEquals("Toyota", result.getBrand());
        assertEquals("Corolla", result.getModel());
        assertEquals(BigDecimal.valueOf(25.0), result.getPrice());
    }

    @Test
    void testFetchCarByIdThrowsCarNotFoundExceptionForNonExistingCar() {
        assertThrows(CarNotFoundException.class, () -> carService.fetchCarById(NON_EXISTING_CAR_ID));
        verify(carRepository, times(1)).findById(NON_EXISTING_CAR_ID);
        verify(carMapper, never()).toDto(any());
        assertEquals(MessageFormat.format(CAR_NOT_FOUND, NON_EXISTING_CAR_ID),
                assertThrows(CarNotFoundException.class,
                        () -> carService.fetchCarById(NON_EXISTING_CAR_ID)).getMessage());
    }
}

