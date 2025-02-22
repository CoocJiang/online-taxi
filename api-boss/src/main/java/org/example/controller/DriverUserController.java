package org.example.controller;


import org.example.dto.DriverUser;
import org.example.dto.ResponseResult;
import org.example.remote.ServiceDriverUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverUserController {
    @Autowired
    ServiceDriverUserClient serviceDriverUserClient;

    /**
     * 添加司机
     * @param driverUser
     * @return
     */
    @PostMapping("/driver-user")
    public ResponseResult adduser(@RequestBody DriverUser driverUser) {
        return serviceDriverUserClient.addUser(driverUser);
    }

    //更新司机信息
    @PostMapping("/driver-user/upadate")
    public ResponseResult updateuser(@RequestBody DriverUser driverUser) {
        return serviceDriverUserClient.updateUser(driverUser);
    }
}
