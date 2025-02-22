package org.example.controller;


import org.example.dto.DriverCarBindingRelationship;
import org.example.dto.ResponseResult;
import org.example.remote.ServiceDriverUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Driver_Car_RelationshipController {


    @Autowired
    ServiceDriverUserClient serviceDriverUserClient;



    //绑定司机和车辆
    @PostMapping("/bind")
    public ResponseResult bind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship){
        return serviceDriverUserClient.bind(driverCarBindingRelationship);
    }

    //解除绑定司机和车辆
    @PostMapping("unbind")
    public ResponseResult unbind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship){
        return serviceDriverUserClient.unbind(driverCarBindingRelationship);
    }
}
