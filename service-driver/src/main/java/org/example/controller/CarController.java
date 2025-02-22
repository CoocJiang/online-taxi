package org.example.controller;


import org.example.dto.Car;
import org.example.dto.ResponseResult;
import org.example.mapper.CarMapper;
import org.example.service.Carservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CarController {

    @Autowired
    Carservice carServicece;

    /**添加车辆
     *
     * @param car
     * @return
     */
    @PutMapping("/car")
    public ResponseResult addCar(@RequestBody Car car){
        return carServicece.addCar(car);
    }

    /**
     * 获取car的id
     * @param carId
     * @return
     */
    @PostMapping("/getcarById")
    public ResponseResult gatCarbyId(@RequestParam Long carId){
       return carServicece.getCarbyId(carId);
    }

}
