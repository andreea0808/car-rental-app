package com.example.carrental.controller;

import com.example.carrental.dto.CarDto;
import com.example.carrental.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(
        name = "Admin Controller",
        description = "Admin Controller Exposes REST APIs for Car Service"
)
public class AdminController {

    private final CarService carService;

    @Operation(
            summary = "Create Car REST API",
            description = "Create Car REST API is used to save car object by ADMIN into database",
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
    @PostMapping("/cars")
    public ResponseEntity<CarDto> createCar(@Validated @RequestBody CarDto carDto) {
        CarDto savedCar = carService.create(carDto);
        return ResponseEntity.ok(savedCar);
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
    @PutMapping("/cars/{id}")
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
    @PatchMapping("/cars/{id}/price")
    public ResponseEntity<CarDto> updatePrice(@PathVariable Long id, @RequestBody @Valid CarDto carDto) {
        CarDto updatedCarDto = carService.updateCarPrice(id, carDto);
        return ResponseEntity.ok(updatedCarDto);
    }
}
