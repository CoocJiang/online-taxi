package org.example.controller;


import org.example.dto.DriverUserWorkStatus;
import org.example.dto.ResponseResult;
import org.example.mapper.DriverUserWorkStatusMapper;
import org.example.service.DriverUserWorkStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverUserWorkStatusController {

    @Autowired
    DriverUserWorkStatusService driverUserWorkStatusService;


    /**
     * 修改司机工作状态
     * @param driverUserWorkStatus
     * @return
     */
    @PostMapping("/driver-user-work-status")
    public ResponseResult ChangeWorkStatus(@RequestBody DriverUserWorkStatus driverUserWorkStatus){
        return  driverUserWorkStatusService.ChangeDriverWorkStatus(driverUserWorkStatus);

    }
}
