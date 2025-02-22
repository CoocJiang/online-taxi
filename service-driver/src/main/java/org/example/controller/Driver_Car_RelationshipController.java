package org.example.controller;


import org.example.dto.DriverCarBindingRelationship;
import org.example.dto.ResponseResult;
import org.example.service.Driver_Car_RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Driver_Car_RelationshipController {

    @Autowired
    Driver_Car_RelationshipService relationshipService;


    //绑定司机和车辆
    @PostMapping("/bind")
    public ResponseResult bind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship){
        return relationshipService.bind(driverCarBindingRelationship);
    }

    //解除绑定司机和车辆
    @PostMapping("/unbind")
    public ResponseResult unbind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship){
        return relationshipService.unbind(driverCarBindingRelationship);
    }

    /**
     * 根据司机id查询司机绑定信息
     * @param driverId
     * @return
     */
    @PostMapping("/getDriverCarRelationShipByDriverId")
    public ResponseResult getDriverCarRelationShip(@RequestParam String driverId){
        return relationshipService.getDriverCarRelationShipByDriverID(driverId);
    }

    /**
     * 根据司机手机号查询司机绑定信息
     * @param driverPhone
     * @return
     */
    @PostMapping("/getDriverCarRelationShipByDriverPhone")
    public ResponseResult<DriverCarBindingRelationship> getDriverCarRelationShipByDriverPhone(@RequestParam String driverPhone){
        return relationshipService.getDriverCarRelationShipByDriverPhone(driverPhone);
    }
}
