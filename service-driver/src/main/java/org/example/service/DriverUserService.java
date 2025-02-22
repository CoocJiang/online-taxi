package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.constant.CommonStatusEnum;
import org.example.constant.DriverCarConstants;
import org.example.dto.DriverUser;
import org.example.dto.DriverUserWorkStatus;
import org.example.dto.ResponseResult;
import org.example.mapper.DirverUserMapper;
import org.example.mapper.DriverUserWorkStatusMapper;
import org.example.request.VerificationCodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class DriverUserService {
    @Autowired
    DirverUserMapper dirverUserMapper;

    @Autowired
    DriverUserWorkStatusMapper driverUserWorkStatusMapper;



    //司机登录功能
    public  ResponseResult login(VerificationCodeDTO verificationCodeDTO) {
        //

        return null;
    }

    /**
     * 添加司机
     * @param driverUser
     * @return
     */
    public ResponseResult add(DriverUser driverUser) {
        //添加时间修改时间
        LocalDateTime currenttime =  LocalDateTime.now();
        driverUser.setGmtCreate(currenttime);
        driverUser.setGmtModified(currenttime);

        //插入数据库
        int insert = dirverUserMapper.insert(driverUser);
        System.out.println(driverUser.toString());

        //新增加司机的同时增加司机的工作状态，默认为收车
        DriverUserWorkStatus driverUserWorkStatus = new DriverUserWorkStatus();
        driverUserWorkStatus.setDriverId(driverUser.getId());
        driverUserWorkStatus.setWorkStatus(DriverCarConstants.DRIVER_WORK_STATUS_STOP);
        driverUserWorkStatus.setGmtCreate(currenttime);
        driverUserWorkStatusMapper.insert(driverUserWorkStatus);


        return ResponseResult.success("插入"+insert+"条成功");
    }

    /**
     * 更新司机信息
     * @param driverUser
     * @return
     */
    public ResponseResult update(DriverUser driverUser) {
        //添加时间修改时间
        LocalDateTime currenttime =  LocalDateTime.now();
        driverUser.setGmtCreate(currenttime);
        driverUser.setGmtModified(currenttime);
        //插入数据库
        int i = dirverUserMapper.updateById(driverUser);
        System.out.println(driverUser.toString());
        return ResponseResult.success("修改"+i+"条成功");
    }


    //查看系统有无这位司机
    /**
     * 查询司机
     * @param driverPhone
     * @return
     */
    public ResponseResult findDriverByPhone(String driverPhone) {
        QueryWrapper<DriverUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_phone",driverPhone);
        queryWrapper.eq("state", DriverCarConstants.DRIVER_STATE_VALID);
        List<DriverUser> driverUsers = dirverUserMapper.selectList(queryWrapper);
        if (driverUsers.isEmpty()){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_NOT_EXITST.getCode(),CommonStatusEnum.DRIVER_NOT_EXITST.getValue());
        }else {
            return ResponseResult.success(driverUsers.get(0));
        }
    }


    /**查询司机信息
     * @param phonenumber
     * @return
     */
    public ResponseResult getDriverDetailByPhone(String phonenumber) {

        QueryWrapper<DriverUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_phone",phonenumber);
        DriverUser driverUser = dirverUserMapper.selectOne(queryWrapper);
        if (driverUser==null){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_NOT_EXITST.getCode(),
                    CommonStatusEnum.DRIVER_NOT_EXITST.getValue());
        }else {
            return ResponseResult.fail(driverUser);
        }
    }
}
