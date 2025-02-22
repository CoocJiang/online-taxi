package org.example.service;


import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.example.dto.Car;
import org.example.dto.ResponseResult;
import org.example.mapper.CarMapper;

import org.example.remote.MapClient;
import org.example.response.TerminalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class Carservice {



    @Autowired
    CarMapper carMapper;

    @Resource
    MapClient mapClient;



    public ResponseResult addCar(Car car){

        //添加时间修改时间
        LocalDateTime currenttime =  LocalDateTime.now();
        car.setGmtCreate(currenttime);
        car.setGmtModified(currenttime);
        carMapper.insert(car);

        //获取终端id
        ResponseResult<TerminalResponse> addterminal = mapClient.addterminal(String.valueOf(car.getVehicleNo()), String.valueOf(car.getId()));
        JSONObject tidobject = JSONObject.fromObject(addterminal.getData());
        String tid = tidobject.getString("tid");
        //获取轨迹id
        ResponseResult addtrack = mapClient.addtrack(tid);
        String trid = (String)addtrack.getData();

        //设置终端id
        car.setTid(tid);
        //设置轨迹id
        car.setTrid(trid);
        
        int i = carMapper.updateById(car);
        return ResponseResult.success(i);
    }




    public ResponseResult getCarbyId(Long carId){
        Car car = carMapper.selectById(carId);
        return ResponseResult.success(car);
    };
}
