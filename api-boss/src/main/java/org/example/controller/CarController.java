package org.example.controller;


import org.example.dto.Car;
import org.example.dto.ResponseResult;
import org.example.remote.ServiceDriverUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController {
    @Autowired
    private ServiceDriverUserClient serviceDriverUserClient;

    @PutMapping("/addcar")
    public ResponseResult addCar(@RequestBody Car car){
        return serviceDriverUserClient.addCar(car);
    }


}
