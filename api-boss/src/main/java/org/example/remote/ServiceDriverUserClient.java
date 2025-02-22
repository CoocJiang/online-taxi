package org.example.remote;


import org.example.dto.Car;
import org.example.dto.DriverCarBindingRelationship;
import org.example.dto.DriverUser;
import org.example.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("service-driver-user")
public interface ServiceDriverUserClient  {
    @PostMapping("/user")
    public ResponseResult addUser(@RequestBody DriverUser driverUser);

    @PostMapping("/user/update")
    public ResponseResult updateUser(@RequestBody DriverUser driverUser);


    @PutMapping("/car")
    public ResponseResult addCar(@RequestBody Car car);


    //绑定司机和车辆
    @PostMapping("/bind")
    public ResponseResult bind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship);

    //解除绑定司机和车辆
    @PostMapping("unbind")
    public ResponseResult unbind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship);
}
