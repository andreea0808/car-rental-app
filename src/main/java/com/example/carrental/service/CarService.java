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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final CarTypeValidator carTypeValidator;
    private static final String CAR_NOT_FOUND = "Car with id {0} not found.";

    public CarDto create(CarDto carDto) throws InvalidCarTypeException {
        if (carTypeValidator.isValid(carDto, null)) {
            // Save the car and return the DTO
            Car car = carMapper.toEntity(carDto);
            carRepository.save(car);
            return carMapper.toDto(car);
        }
        throw new InvalidCarTypeException("Invalid car type: " + carDto.getCarType());
    }

    public CarDto replaceCar(CarDto carDto, Long id) throws CarNotFoundException, InvalidCarTypeException {
        Car car = carMapper.toEntity(carDto);
        carRepository.findById(id)
                .orElseThrow(
                        () -> new CarNotFoundException(MessageFormat
                                .format(CAR_NOT_FOUND, id))
                );
        // Check if updated price is valid for the car type
        boolean isPriceValid = carTypeValidator.isValid(carDto, null);
        if (!isPriceValid) {
            throw new InvalidCarTypeException("Invalid price for car type");
        }

        // Update car price
        car.setPrice(carDto.getPrice());
        carRepository.save(car);

        return carMapper.toDto(car);
    }

    public CarDto updateCarPrice(Long id, CarDto carDto) throws CarNotFoundException, InvalidCarTypeException {
        //convert dto to entity
        Car existingCar = carMapper.toEntity(carDto);
        existingCar.setId(carDto.getId());
        //find entity by id in database or throw exception
        Car existingCarToUpdate = carRepository.findById(id)
                .orElseThrow(
                        () -> new CarNotFoundException(MessageFormat
                                .format(CAR_NOT_FOUND, id)));

        if (carTypeValidator.isValid(carDto, null)) {
            carMapper.updateCar(carDto, existingCarToUpdate);
            Car carToSave = carRepository.save(existingCarToUpdate);
            return carMapper.toDto(carToSave);
        }
        throw new InvalidCarTypeException("Invalid car type: " + carDto.getCarType());
    }

    public Map<CarType, Long> countTypes() {
        return carRepository.countTypes()
                .stream()
                .collect(
                        toMap(
                                NumberOfTypes::getType,
                                NumberOfTypes::getNr
                        )
                );
    }

    // Ideally, this should return Car since method might be used in other services and you won't wanna use dtos
    public CarDto fetchCarById(Long id) {
        return carRepository.findById(id)
                .map(carMapper::toDto)
                .orElseThrow(
                        () -> new CarNotFoundException(MessageFormat
                                .format(CAR_NOT_FOUND, id)));
    }

    public Page<CarDto> findAllCarsSortedByPrice(Pageable pageable) {
        Page<Car> cars = carRepository.findAllByOrderByPriceAsc(pageable);
        List<CarDto> carDtoList = cars.get().map(this::convertToDto).toList();
        return new PageImpl<>(carDtoList, cars.getPageable(), cars.getTotalElements());
    }

    public Page<CarDto> findAllByCarTypeOrderByPriceAsc(CarType carType, Pageable pageable) {
        Page<Car> cars = carRepository.findAllByCarTypeOrderByPriceAsc(carType, pageable);
        List<CarDto> carDtoList = cars.get().map(this::convertToDto).toList();
        return new PageImpl<>(carDtoList, cars.getPageable(), cars.getTotalElements());
    }

    private CarDto convertToDto(Car car) {
        return carMapper.toDto(car);
    }
}
