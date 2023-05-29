package com.example.carrental.repository;

import com.example.carrental.model.Car;
import com.example.carrental.model.CarType;
import com.example.carrental.util.NumberOfTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query(value = "select car_type as type, count(*) as nr from car group by car_type order by car_type", nativeQuery = true)
    List<NumberOfTypes> countTypes();

    Page<Car> findAllByOrderByPriceAsc(Pageable pageable);
    Page<Car> findAllByCarTypeOrderByPriceAsc(CarType carType, Pageable pageable);

}