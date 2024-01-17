package com.eloi.example.v1.services;

import com.eloi.example.v1.dto.requests.CreateCarDTO;
import com.eloi.example.v1.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface CarService {
public ResponseEntity<ApiResponse>createCar(CreateCarDTO createCarDTO) throws  Exception;

}
