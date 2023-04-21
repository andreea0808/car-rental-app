package com.example.carrental.validation;

import com.example.carrental.dto.CarDto;
import com.example.carrental.exceptions.InvalidCarTypeException;
import com.example.carrental.model.CarType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
@ConfigurationProperties("app")
public class CarTypeValidator implements ConstraintValidator<ValidCarType, CarDto> {

    @Value("${app.economyMax}")
    private BigDecimal economyMax;

    @Value("${app.standardMax}")
    private BigDecimal standardMax;

    @Value("${app.suvMin}")
    private BigDecimal suvMin;

    @Override
    public boolean isValid(CarDto carDto, ConstraintValidatorContext context) {
        if (carDto == null) {
            return true; // null values should be validated by @NotNull annotation
        }

        BigDecimal price = carDto.getPrice();
        CarType carType = carDto.getCarType();

        switch (carType) {
            case ECONOMY -> {
                if (economyMax.compareTo(price) <= 0) {
                    addErrorMessage(context, CarType.ECONOMY, economyMax);
                    return false;
                }
            }
            case STANDARD -> {
                if ((price.compareTo(economyMax) < 0) || (price.compareTo(standardMax) >= 0)) {
                    addErrorMessage(context, economyMax, standardMax);
                    return false;
                }
            }
            case SUV -> {
                if (price.compareTo(suvMin) < 0) {
                    addErrorMessage(context, CarType.SUV, suvMin);
                    return false;
                }
            }
            default -> throw new InvalidCarTypeException("Invalid car type: " + carType);
        }

        return true;
    }

    private void addErrorMessage(ConstraintValidatorContext context, CarType carType, BigDecimal economyMax) {
        String message = String.format("Invalid price for %s car type. Price must be greater than %s.", carType, economyMax);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

    private void addErrorMessage(ConstraintValidatorContext context, BigDecimal economyMax, BigDecimal standardMax) {
        String message = String.format("Invalid price for %s car type. Price must be between %s and %s.", CarType.STANDARD, economyMax, standardMax);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

}