package org.example.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.constant.CommonStatusEnum;
import org.example.constant.DriverCarConstants;
import org.example.dto.DriverUserWorkStatus;
import org.example.dto.ResponseResult;
import org.example.mapper.DriverUserWorkStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DriverUserWorkStatusService {


    @Autowired
    DriverUserWorkStatusMapper driverUserWorkStatusMapper;

    /**
     * 修改司机工作状态
     * @param driverUserWorkStatus
     * @return
     */
    public ResponseResult ChangeDriverWorkStatus(DriverUserWorkStatus driverUserWorkStatus){
        LocalDateTime localDateTime = LocalDateTime.now();
        Map<String,Object> map = new HashMap<>();
//        map.put("driver_id",driverUserWorkStatus.getDriverId());
//        List<DriverUserWorkStatus> list = driverUserWorkStatusMapper.selectByMap(map);
        QueryWrapper<DriverUserWorkStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_id",driverUserWorkStatus.getDriverId());
        List<DriverUserWorkStatus> list = driverUserWorkStatusMapper.selectList(queryWrapper);
        if (list.isEmpty()){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_NOT_EXITST.getCode(),CommonStatusEnum.DRIVER_NOT_EXITST.getValue());
        }
        DriverUserWorkStatus driverUserWorkStatus1 = list.get(0);
        driverUserWorkStatus1.setWorkStatus(driverUserWorkStatus.getWorkStatus());
        driverUserWorkStatus1.setGmtModified(localDateTime);
        driverUserWorkStatusMapper.updateById(driverUserWorkStatus1);
        return ResponseResult.success("修改成功");
    }

}
