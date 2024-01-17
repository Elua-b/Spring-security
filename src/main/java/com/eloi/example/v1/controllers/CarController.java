package com.eloi.example.v1.controllers;

import com.eloi.example.v1.dto.requests.CreateCarDTO;
import com.eloi.example.v1.payload.ApiResponse;
import com.eloi.example.v1.serviceImpls.CarServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/car")
@RequiredArgsConstructor
public class CarController {

    public final  CarServiceImpl carService;
    public final CreateCarDTO createCarDTO;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCar(@RequestBody  CreateCarDTO createCarDTO) throws Exception{
        return carService.createCar(createCarDTO);
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCars() throws  Exception{
        return (ResponseEntity<ApiResponse>) carService.getAllCars();
    }
   @PutMapping("/cars/{carId}")
    public <carId> ResponseEntity<ApiResponse> updateCar(@PathVariable Long carId, @RequestBody CreateCarDTO updatedCarDTO) throws Exception{
        return  carService.updateCar(carId,updatedCarDTO);
   }
   @GetMapping("/cars/{carId}")
    public <CarId> ResponseEntity<ApiResponse> getCarById(@PathVariable Long carId) throws  Exception{
        return  carService.getCarById(carId);
   }
    @DeleteMapping("/cars/{carId}")
    public <CarId> ResponseEntity<ApiResponse> deleteCar(@PathVariable Long carId) throws  Exception {
        return carService.deleteCar(carId);
    }

}

