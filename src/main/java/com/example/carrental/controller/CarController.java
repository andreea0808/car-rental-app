package com.example.carrental.controller;

import com.example.carrental.dto.CarDto;
import com.example.carrental.model.CarType;
import com.example.carrental.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<CarDto> createCar(@Validated @RequestBody CarDto carDto) {
        CarDto savedCar = carService.create(carDto);
        //return ResponseEntity.ok(savedCar); // status 200
        return new ResponseEntity<>(savedCar, HttpStatus.CREATED); // 201
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDto> fetchCarById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(carService.fetchCarById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<CarDto> replaceCar(
            @PathVariable Long id,
            @Validated @RequestBody CarDto carDto
    ) {
        return ResponseEntity.ok(carService.replaceCar(carDto, id));
    }

    @PatchMapping("/{id}/price")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<CarDto> updatePrice(
            @PathVariable Long id,
            @RequestBody @Valid CarDto carDto
    ) {
        CarDto updatedCarDto = carService.updateCarPrice(id, carDto);
        return ResponseEntity.ok(updatedCarDto);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<CarType, Long>> count() {
        return ResponseEntity.ok(carService.countTypes());
    }

    @GetMapping("/car-list")
    public ResponseEntity<Page<CarDto>> findAllCars(Pageable pageable) {
        Page<CarDto> carDtos = carService.findAllCarsSortedByPrice(pageable);
        return ResponseEntity.ok(carDtos);
    }
}
