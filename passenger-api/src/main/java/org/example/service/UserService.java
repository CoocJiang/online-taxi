package org.example.service;

import org.example.constant.CommonStatusEnum;
import org.example.dto.PassengerUser;
import org.example.dto.ResponseResult;
import org.example.dto.TokenResult;
import org.example.remote.ServicePassengerUserClient;
import org.example.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    ServicePassengerUserClient servicePassengerUserClient;


    public ResponseResult getUsertoken(String stoken){
        //解析token，拿到手机号
        TokenResult tokenResult = JWTUtils.parseToken(stoken);
        //无需判断tokenresult为null拦截器会为你判断
        String passengerPhone = tokenResult.getPhone();
        //利用手机号查询用户信息
        return servicePassengerUserClient.getuser(passengerPhone);
    }
}
