package com.jazzybruno.example.v1.serviceImpls;

import com.jazzybruno.example.v1.dto.requests.CreateCarDTO;
import com.jazzybruno.example.v1.models.Car;
import com.jazzybruno.example.v1.models.User;
import com.jazzybruno.example.v1.payload.ApiResponse;
import com.jazzybruno.example.v1.repositories.CarRepository;
import com.jazzybruno.example.v1.repositories.UserRepository;
import com.jazzybruno.example.v1.security.user.UserSecurityDetails;
import com.jazzybruno.example.v1.services.CarService;
import com.jazzybruno.example.v1.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Optional;
@Service
@Component

public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    @Autowired
    public CarServiceImpl(CarRepository carRepository , UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<ApiResponse> createCar(CreateCarDTO createCarDTO) throws Exception {
        if (UserUtils.isUserLoggedIn()) {
            UserSecurityDetails loggedInUser = UserUtils.getLoggedInUser();
            Optional<User> optionalUser = null;
            if(loggedInUser != null){
                optionalUser = userRepository.findUserByEmail(loggedInUser.username);
            }else{

                return ResponseEntity.status(401).body(new ApiResponse(
                        false,
                        "User not authenticated"
                ));
            }
            Optional<Car> existingCar = carRepository.findByCarName(createCarDTO.getCarName());

            if (existingCar.isEmpty()) {
                Car car = new Car(
                        createCarDTO.getCarName(),
                        createCarDTO.getPrice(),
                        createCarDTO.getYear()
                );

                car.setUser(optionalUser.get()); // Associate the car with the logged-in user
                try {
                    carRepository.save(car);

                    return ResponseEntity.ok().body(new ApiResponse(
                            true,
                            "Successfully saved the car",
                            car
                    ));

                } catch (HttpServerErrorException.InternalServerError ex) {
                    return ResponseEntity.status(500).body(new ApiResponse(
                            false,
                            "Failed to create the Car"
                    ));
                }
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(
                        false,
                        "The Car with the name: " + createCarDTO.getCarName() + " already exists"
                ));
            }
        } else {
            return ResponseEntity.status(401).body(new ApiResponse(
                    false,
                    "User not authenticated"
            ));
        }
    }
    public ResponseEntity<ApiResponse> getAllCars() {
        List<Car> cars = carRepository.findAll();

        if (cars.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse(
                    false,
                    "No cars found"
            ));
        }

        return ResponseEntity.ok().body(new ApiResponse(
                true,
                "List of cars",
                cars
        ));
    }
    public ResponseEntity<ApiResponse> updateCar(Long carId, CreateCarDTO updatedCarDTO) {

        if (UserUtils.isUserLoggedIn()) {

            Optional<Car> optionalCar = carRepository.findById(carId);
            if (optionalCar.isPresent()) {
                Car car = optionalCar.get();


                car.setCarName(updatedCarDTO.getCarName());
                car.setPrice(updatedCarDTO.getPrice());
                car.setYear(updatedCarDTO.getYear());


                try {
                    carRepository.save(car);
                    return ResponseEntity.ok().body(new ApiResponse(
                            true,
                            "Car updated successfully",
                            car
                    ));
                } catch (Exception e) {
                    return ResponseEntity.status(500).body(new ApiResponse(
                            false,
                            "Failed to update the car"
                    ));
                }
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(
                        false,
                        "Car with ID " + carId + " not found"
                ));
            }
        } else {
            return ResponseEntity.status(401).body(new ApiResponse(
                    false,
                    "User not authenticated"
            ));
        }
    }

}
