package com.example.carrental.mapper;

import com.example.carrental.dto.CarDto;
import com.example.carrental.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper {

    CarDto toDto(Car car);

    Car toEntity(CarDto dto);

    List<CarDto> toDtoList(List<Car> cars);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "model", ignore = true)
    @Mapping(target = "color", ignore = true)
    @Mapping(target = "carType", ignore = true)
    Car updateCar(CarDto dto, @MappingTarget Car car);

}