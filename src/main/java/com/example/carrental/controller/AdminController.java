package com.example.carrental.controller;

import com.example.carrental.dto.CarDto;
import com.example.carrental.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Management")
public class AdminController {

    private final CarService carService;


    @Operation(
            description = "Post endpoint for manager",
            responses = {
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping("/cars")
    @ResponseStatus(HttpStatus.CREATED)
    public String createCar(@Validated @RequestBody CarDto carDto) {
        CarDto savedCar = carService.create(carDto);
        return String.format("The car with id %d has been saved", savedCar.getId());
    }

    @Operation(
            description = "Put endpoint for manager",
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
    @PutMapping("/cars/{id}")
    public ResponseEntity<CarDto> replaceCar(
            @PathVariable Long id,
            @Validated @RequestBody CarDto carDto
    ) {
        return ResponseEntity.ok(carService.replaceCar(carDto, id));
    }

    @Operation(
            description = "Patch endpoint for manager",
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
    @PatchMapping("/cars/{id}/price")
    public ResponseEntity<CarDto> updatePrice(
            @PathVariable Long id,
            @RequestBody @Valid CarDto carDto
    ) {
        CarDto updatedCarDto = carService.updateCarPrice(id, carDto);
        return ResponseEntity.ok(updatedCarDto);
    }
}
