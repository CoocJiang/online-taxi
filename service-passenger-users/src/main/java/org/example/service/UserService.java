package org.example.service;


import org.example.dto.PassengerUser;
import org.example.dto.ResponseResult;
import org.example.mapper.PassengerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    PassengerMapper passengerMapper;

    //查询用户是否存在，若不存在就注册，存在就do nothing
    public ResponseResult RegOrLogin(String passengerPhonenumer){
        //查询数据库有无
        Map<String, Object> map = new HashMap<>();
        map.put("passenger_phone",passengerPhonenumer);
        //查询数据库
        List<PassengerUser> users = passengerMapper.selectByMap(map);
        System.out.println(users.toString());
        //没有就注册进入数据库
       if (users.size()==0){
           PassengerUser passengerUser =new PassengerUser();
           //默认设置用户名为
           passengerUser.setPassengerName("用户"+passengerPhonenumer);
           passengerUser.setPassengerPhone(passengerPhonenumer);
           //获取当前时间
           passengerUser.setGmtCreate(LocalDateTime.now());//设置注册时间
           passengerUser.setGmtModified(LocalDateTime.now());//设置修改时间
           passengerMapper.insert(passengerUser);//插入用户
       }
        return null;
    }


    //查询所有用户
    public ResponseResult getuser(String passengerPhone){
        Map<String, Object> map = new HashMap<>();
        map.put("passenger_phone",passengerPhone);
        List<PassengerUser>  list= passengerMapper.selectByMap(map);
        return ResponseResult.success(list.get(0));
    }

}
