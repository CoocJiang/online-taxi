package org.example.controller;


import net.sf.json.JSONObject;
import org.example.constant.DriverCarConstants;
import org.example.dto.DriverUser;
import org.example.dto.ResponseResult;
import org.example.mapper.DirverUserMapper;
import org.example.request.VerificationCodeDTO;
import org.example.response.DriverUserExistsResponse;
import org.example.response.NumberCodeResponse;
import org.example.service.CityDriverUserService;
import org.example.service.DriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DriverUserController {
    @Autowired
    DriverUserService driverUserService;

    @Autowired
    DirverUserMapper dirverUserMapper;

    @Autowired
    CityDriverUserService cityDriverUserService;

    //添加司机
    @PostMapping("/user")
    public ResponseResult addDriver(@RequestBody DriverUser driverUser){
        return driverUserService.add(driverUser);
    }

    //更新司机信息
    @PostMapping("/user/update")
    public ResponseResult updateDriver(@RequestBody DriverUser driverUser){
        return driverUserService.update(driverUser);
    }


    //为什么使用Path的方式会出错？
    @PostMapping("/findDriverByPhone2/{driverPhone}")
    public ResponseResult findDriverByPhone(@PathVariable String driverPhone){
        ResponseResult<DriverUser> driverUserResponseResult =  driverUserService.findDriverByPhone(driverPhone);
        int Exists = DriverCarConstants.DRIVER_EXISTS;
        if (driverUserResponseResult.getData()==null){
            Exists = DriverCarConstants.DRIVER_NOT_EXISTS;
        }
        DriverUserExistsResponse response = new DriverUserExistsResponse(driverUserResponseResult.getData().getDriverPhone(),Exists);
        return ResponseResult.success(response);
    }

    //根据手机号判断司机是否存在
    @PostMapping("/findDriverByPhone")
    public ResponseResult findDriverByPhone(@RequestBody VerificationCodeDTO verificationCodeDTO){
        ResponseResult<DriverUser> driverUserResponseResult =  driverUserService.findDriverByPhone(verificationCodeDTO.getDriverPhone());
        int Exists = DriverCarConstants.DRIVER_EXISTS;
        if (driverUserResponseResult.getData()==null){
            Exists = DriverCarConstants.DRIVER_NOT_EXISTS;
        }
        DriverUserExistsResponse response;
        if (driverUserResponseResult.getData()==null){
            response = new DriverUserExistsResponse(null,Exists);
        }else {
           response = new DriverUserExistsResponse(driverUserResponseResult.getData().getDriverPhone(),Exists);
        }
        return ResponseResult.success(response);
    }

    //根据城市查询该城市有无司机
    @PostMapping("/isAvailableDriver")
    public boolean isAvailableDriver(@RequestParam String cityCode){
        return cityDriverUserService.isAvailableDriver(cityCode);
    }


    //根据车id查询司机是否可接单
    @PostMapping("/AvailableDriver/get")
    public ResponseResult getAvailableDriver(@RequestParam Long carId){
        return cityDriverUserService.getAvailableDriver(carId);
    }


    /**
     * 根据手机号查询司机信息
     * @param phonenumber
     * @return
     */
    @PostMapping("/diverDetail/get")
    public ResponseResult getDriverDetailByPhone(@RequestParam String phonenumber){
        return driverUserService.getDriverDetailByPhone(phonenumber);
    }
}
