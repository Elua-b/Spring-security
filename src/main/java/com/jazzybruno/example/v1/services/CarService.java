package com.jazzybruno.example.v1.services;

import com.jazzybruno.example.v1.dto.requests.CreateCarDTO;
import com.jazzybruno.example.v1.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface CarService {
public ResponseEntity<ApiResponse>createCar(CreateCarDTO createCarDTO) throws  Exception;

}
