package org.example.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.constant.CommonStatusEnum;
import org.example.constant.DriverCarConstants;
import org.example.dto.*;
import org.example.mapper.CarMapper;
import org.example.mapper.DirverUserMapper;
import org.example.mapper.DriverCarBindingRelationshipMapper;
import org.example.mapper.DriverUserWorkStatusMapper;
import org.example.response.OrderDriverResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class CityDriverUserService {

    @Autowired
    DirverUserMapper dirverUserMapper;

    @Autowired
    CarMapper carMapper;

    public boolean isAvailableDriver(String cityCode){
        int count = dirverUserMapper.selectDriverUserbyCityCode(cityCode);
        if (count>0){
            return true;
        }else {
            return  false;
        }
    }


    @Autowired
    DriverCarBindingRelationshipMapper driverCarBindingRelationshipMapper;


    @Autowired
    DriverUserWorkStatusMapper driverUserWorkStatusMapper;

    //查询可用接客的司机
    public ResponseResult getAvailableDriver(Long carId) {
        //根据carid查询绑定状态为1的司机id
        QueryWrapper<DriverCarBindingRelationship> driverCarBindingRelationshipQueryWrapper = new QueryWrapper<>();
        driverCarBindingRelationshipQueryWrapper.eq("car_id",carId)
                .eq("bind_state", DriverCarConstants.DRIVER_CAR_BIND);
        DriverCarBindingRelationship driverCarBindingRelationship =
                driverCarBindingRelationshipMapper.selectOne(driverCarBindingRelationshipQueryWrapper);

        if (driverCarBindingRelationship==null){
            return ResponseResult.fail(CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getCode(),
                    CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getValue());
        }
        Long driverId = driverCarBindingRelationship.getDriverId();

        //查询状态为出车的司机
        QueryWrapper<DriverUserWorkStatus> driverUserWorkStatusQueryWrapper = new QueryWrapper<>();
        driverUserWorkStatusQueryWrapper.eq("work_status",DriverCarConstants.DRIVER_WORK_STATUS_START)
                .eq("driver_id",driverId);
        DriverUserWorkStatus driverUserWorkStatus = driverUserWorkStatusMapper.selectOne(driverUserWorkStatusQueryWrapper);
        if (driverUserWorkStatus==null){
            return ResponseResult.fail(CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getCode(),
                    CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getValue());
        }

        //根据司机id查询司机信息
        OrderDriverResponse orderDriverResponse = new OrderDriverResponse();
        orderDriverResponse.setDriverId(driverId);
        QueryWrapper<DriverUser> driverUserQueryWrapper = new QueryWrapper<>();
        driverUserQueryWrapper.eq("id",driverId);
        DriverUser driverUser = dirverUserMapper.selectOne(driverUserQueryWrapper);

        //查询车辆信息
        QueryWrapper<Car> carQueryWrapper = new QueryWrapper<>();
        carQueryWrapper.eq("id",carId);
        Car car = carMapper.selectOne(carQueryWrapper);

        if (driverUser == null) {
            return ResponseResult.fail(CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getCode(),
                    CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getValue());
        }
        orderDriverResponse.setDriverPhone(driverUser.getDriverPhone());
        orderDriverResponse.setDriverId(driverId);
        orderDriverResponse.setCarId(carId);
        orderDriverResponse.setLicenseId(driverUser.getLicenseId());
        orderDriverResponse.setVehicleNo(car.getVehicleNo());
        orderDriverResponse.setVehicleType(car.getVehicleType());
        return ResponseResult.success(orderDriverResponse);
    }

}
