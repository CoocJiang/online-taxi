package org.example.controller;


import org.example.dto.DriverUser;
import org.example.dto.ResponseResult;
import org.example.remote.ServiceDriverClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverController {

    @Autowired
    ServiceDriverClient serviceDriverClient;


    //司机更改信息
    @PutMapping("/driver/update")
    public ResponseResult uodateDriver(@RequestBody DriverUser driverUser){
        return serviceDriverClient.uodateDriver(driverUser);
    }


    //司机登录功能


}
