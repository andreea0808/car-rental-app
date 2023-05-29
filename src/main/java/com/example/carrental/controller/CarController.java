package com.example.carrental.controller;

import com.example.carrental.dto.CarDto;
import com.example.carrental.model.CarType;
import com.example.carrental.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(path = "/api/v1/cars")
@RequiredArgsConstructor
@Tag(
        name = "Car Controller",
        description = "Car Controller Exposes REST APIs for Car Service"
)
public class CarController {

    private final CarService carService;

    @Operation(
            summary = "Create Car REST API by ADMIN",
            description = "Create Car REST API is used to save car object by ADMIN into database",
            responses = {
                    @ApiResponse(
                            description = "OK",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<CarDto> createCar(@Validated @RequestBody CarDto carDto) {
        CarDto savedCar = carService.create(carDto);
        return ResponseEntity.ok(savedCar);
    }

    @Operation(
            summary = "GET Car By Id",
            description = "Get Car by Id REST API is used to fetch car object by id by USER",
            responses = {
                    @ApiResponse(
                            description = "Ok",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCarById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(carService.fetchCarById(id));
    }

    @Operation(
            summary = "Update fields by ADMIN",
            description = "Put REST API  is used to update car's fields by ADMIN into database",
            responses = {
                    @ApiResponse(
                            description = "Ok",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "403"
                    )
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<CarDto> replaceCar(@PathVariable Long id, @Validated @RequestBody CarDto carDto) {
        return ResponseEntity.ok(carService.replaceCar(carDto, id));
    }

    @Operation(
            summary = "Update price by ADMIN",
            description = "Patch REST API is used to update the price field by ADMIN into database",
            responses = {
                    @ApiResponse(
                            description = "Ok",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "403"
                    )
            }
    )
    @PatchMapping("/{id}/price")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<CarDto> updatePrice(@PathVariable Long id, @RequestBody @Valid CarDto carDto) {
        CarDto updatedCarDto = carService.updateCarPrice(id, carDto);
        return ResponseEntity.ok(updatedCarDto);
    }

    @Operation(
            summary = "Fetch Number Of Cars By Car Type",
            description = "Get endpoint for USER, used to count the number of cars form database by car type",
            responses = {
                    @ApiResponse(
                            description = "Ok",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/count")
    public ResponseEntity<Map<CarType, Long>> count() {
        return ResponseEntity.ok(carService.countTypes());
    }

    @Operation(
            description = "Get endpoint for USER, used to retrieve cars sorted by price from database",
            responses = {
                    @ApiResponse(
                            description = "Ok",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/car-list")
    public ResponseEntity<Page<CarDto>> findAllCars(Pageable pageable) {
        Page<CarDto> carDtos = carService.findAllCarsSortedByPrice(pageable);
        return ResponseEntity.ok(carDtos);
    }

    @Operation(
            summary = "Fetch Cars By Car Type",
            description = "Get endpoint for USER, used to fetch cars by car type",
            responses = {
                    @ApiResponse(
                            description = "Ok",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/car-type")
    public ResponseEntity<Page<CarDto>> findAllByCarType(CarType carType, Pageable pageable) {
        Page<CarDto> carDtos = carService.findAllByCarTypeOrderByPriceAsc(carType, pageable);
        return ResponseEntity.ok(carDtos);
    }
}