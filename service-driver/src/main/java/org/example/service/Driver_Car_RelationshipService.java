package org.example.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.constant.CommonStatusEnum;
import org.example.constant.DriverCarConstants;
import org.example.dto.DriverCarBindingRelationship;
import org.example.dto.DriverUser;
import org.example.dto.ResponseResult;
import org.example.mapper.DriverCarBindingRelationshipMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class Driver_Car_RelationshipService {
    @Autowired
    DriverCarBindingRelationshipMapper driverCarBindingRelationshipMapper;

    @Autowired
    DriverUserService driverUserService;


    /**绑定司机和车
     * @param driverCarBindingRelationship
     * @return
     */
    public ResponseResult bind(DriverCarBindingRelationship driverCarBindingRelationship) {
        //先查询司机和车辆是否已经绑定
        QueryWrapper<DriverCarBindingRelationship> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_id", driverCarBindingRelationship.getDriverId());

        //获取司机的绑定信息
        List<DriverCarBindingRelationship> driverCarBindingRelationships1 = driverCarBindingRelationshipMapper.selectList(queryWrapper);
        //获取汽车的绑定信息
        List<DriverCarBindingRelationship> driverCarBindingRelationships2 =
                driverCarBindingRelationshipMapper
                        .selectList(new QueryWrapper<DriverCarBindingRelationship>().
                                eq("car_id", driverCarBindingRelationship.getCarId()));
        //首先保证数据库有数据
        if (driverCarBindingRelationships1.size()!= 0 && driverCarBindingRelationships2.size()!= 0) {
            //如果司机已将绑定了，并且绑定的汽车就是需要绑定的这一辆,并且装态为已绑定
            if (driverCarBindingRelationships1.get(0).getCarId() == driverCarBindingRelationship.getCarId()) {
                //如果状态为没有绑定，重新绑定他们
                if (driverCarBindingRelationships1.get(0).getBindState() == DriverCarConstants.DRIVER_CAR_UNBIND) {
                    driverCarBindingRelationship.setBindState(DriverCarConstants.DRIVER_CAR_BIND);
                    LocalDateTime localDateTime = LocalDateTime.now();
                    driverCarBindingRelationship.setUnBindingTime(localDateTime);
                    driverCarBindingRelationshipMapper.updateById(driverCarBindingRelationship);
                    return ResponseResult.success(CommonStatusEnum.SUCCESS);
                } else {
                    return ResponseResult.fail(CommonStatusEnum.DRIVER_CAR_BIND_EXISTS.getCode(),CommonStatusEnum.DRIVER_CAR_BIND_EXISTS.getValue());
                }
            }
        }
        //如果汽车绑定了，司机没有绑定
         if (driverCarBindingRelationships2.size()!=0&&
                driverCarBindingRelationships2.get(0).getDriverId() != driverCarBindingRelationship.getDriverId()) {
//            并且绑定有效，
            if (driverCarBindingRelationships2.get(0).getBindState() == DriverCarConstants.DRIVER_CAR_BIND){
                return ResponseResult.fail(CommonStatusEnum.CAR_BIND_EXISTS.getCode(),CommonStatusEnum.CAR_BIND_EXISTS.getValue());
            }else {
                //可以重新绑定
                DriverCarBindingRelationship driverCarBindingRelationship2 = driverCarBindingRelationships2.get(0);
                driverCarBindingRelationship2.setDriverId(driverCarBindingRelationship.getDriverId());
                LocalDateTime localDateTime = LocalDateTime.now();
                driverCarBindingRelationship2.setUnBindingTime(localDateTime);
                driverCarBindingRelationship2.setBindState(DriverCarConstants.DRIVER_CAR_BIND);
                driverCarBindingRelationshipMapper.updateById(driverCarBindingRelationship2);
            }
        }
        //司机绑定了，汽车没有绑定
         if (driverCarBindingRelationships1.size()!=0&&
                driverCarBindingRelationships1.get(0).getCarId()!=driverCarBindingRelationship.getCarId()) {
            //如果绑定有效
            if (driverCarBindingRelationships1.get(0).getBindState()==DriverCarConstants.DRIVER_CAR_BIND){
                return ResponseResult.fail(CommonStatusEnum.DRIVER_BIND_EXISTS.getCode(),CommonStatusEnum.DRIVER_BIND_EXISTS.getValue());
            }else {
                //可以重新绑定
                DriverCarBindingRelationship driverCarBindingRelationship1 = driverCarBindingRelationships1.get(0);
                driverCarBindingRelationship1.setDriverId(driverCarBindingRelationship.getCarId());
                LocalDateTime localDateTime = LocalDateTime.now();
                driverCarBindingRelationship1.setUnBindingTime(localDateTime);
                driverCarBindingRelationship1.setBindState(DriverCarConstants.DRIVER_CAR_BIND);
                driverCarBindingRelationshipMapper.updateById(driverCarBindingRelationship1);
            }
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        driverCarBindingRelationship.setBindingTime(localDateTime);
        driverCarBindingRelationship.setBindState(DriverCarConstants.DRIVER_CAR_BIND);
        int insert = driverCarBindingRelationshipMapper.insert(driverCarBindingRelationship);
        return ResponseResult.success(insert);
    }

    /**
     * 解绑司机和车
     * @param driverCarBindingRelationship
     * @return
     */
    public ResponseResult unbind(DriverCarBindingRelationship driverCarBindingRelationship){
        //先确保有这条信息
        QueryWrapper<DriverCarBindingRelationship> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_id",driverCarBindingRelationship.getDriverId()).eq("car_id",driverCarBindingRelationship.getCarId());
        List<DriverCarBindingRelationship> driverCarBindingRelationships = driverCarBindingRelationshipMapper.selectList(queryWrapper);
        if (driverCarBindingRelationships.size()!=0){
            DriverCarBindingRelationship driverCarBindingRelationship1 = driverCarBindingRelationships.get(0);
            LocalDateTime localDateTime = LocalDateTime.now();
            driverCarBindingRelationship1.setUnBindingTime(localDateTime);
            driverCarBindingRelationship1.setBindState(DriverCarConstants.DRIVER_CAR_UNBIND);
            int insert = driverCarBindingRelationshipMapper.updateById(driverCarBindingRelationship1);
            return ResponseResult.success(insert);
        }else {
            return ResponseResult.fail(CommonStatusEnum.DRIVER_CAR_BIND_NOT_EXISTS.getCode(),CommonStatusEnum.DRIVER_CAR_BIND_NOT_EXISTS.getValue());
        }

    }

    /**
     * 根据司机id查询司机绑定信息
     * @param driverId
     * @return
     */
    public ResponseResult<DriverCarBindingRelationship> getDriverCarRelationShipByDriverID(String driverId) {
        QueryWrapper<DriverCarBindingRelationship> queryWrapper = new QueryWrapper<>();
        queryWrapper.select().eq("driver_id",driverId);
        DriverCarBindingRelationship driverCarBindingRelationship = driverCarBindingRelationshipMapper.selectOne(queryWrapper);
        return ResponseResult.success(driverCarBindingRelationship);
    }
    /**
     * 根据司机手机号查询司机绑定信息
     * @param DriverPhone
     * @return
     */
    public ResponseResult<DriverCarBindingRelationship> getDriverCarRelationShipByDriverPhone(String DriverPhone) {
        ResponseResult<DriverUser> driverUser = driverUserService.getDriverDetailByPhone(DriverPhone);
        return getDriverCarRelationShipByDriverID(String.valueOf(driverUser.getData().getId()));
    }
}
