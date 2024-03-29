package com.eloi.example.v1.repositories;

import com.eloi.example.v1.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
Optional<Car> findByCarName( String carName);
}
