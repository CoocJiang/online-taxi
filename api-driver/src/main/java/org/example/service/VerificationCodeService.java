package org.example.service;


import net.sf.json.JSONObject;
import org.example.constant.CommonStatusEnum;
import org.example.constant.DriverCarConstants;
import org.example.constant.IdentityConstants;
import org.example.dto.ResponseResult;
import org.example.remote.ServiceDriverClient;
import org.example.remote.ServiceVefificationcodeClient;
import org.example.request.VerificationCodeDTO;
import org.example.response.DriverUserExistsResponse;
import org.example.response.NumberCodeResponse;
import org.example.response.TokenResponse;
import org.example.utils.JWTUtils;
import org.example.utils.RedisPrefixUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {

    @Autowired
    ServiceDriverClient serviceDriverClient;

    @Autowired
    ServiceVefificationcodeClient serviceVefificationcodeClient;

    @Autowired
    RedisTemplate redisTemplate;

    //调用微服务获取验证码
    public ResponseResult<NumberCodeResponse> generatorCode(VerificationCodeDTO verificationCodeDTO) {
        //先判断用户是不是是否存在
        ResponseResult<DriverUserExistsResponse> driverByPhone= serviceDriverClient.findDriverByPhone(verificationCodeDTO);
        JSONObject jsonObject = JSONObject.fromObject(driverByPhone.getData());
        if (jsonObject.getInt("ifExists")== DriverCarConstants.DRIVER_NOT_EXISTS){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_NOT_EXITST.getCode(),CommonStatusEnum.DRIVER_NOT_EXITST.getValue());
        }
//        获取验证码
        ResponseResult<NumberCodeResponse> numberCodeResponseResponseResult = serviceVefificationcodeClient.numbercode();
        JSONObject responseData = JSONObject.fromObject(numberCodeResponseResponseResult.getData());
        System.out.println("验证码"+responseData.getString("numbercode"));
        String numbercode = responseData.getString("numbercode");
        System.out.println(verificationCodeDTO.getDriverPhone());
        System.out.println("存入redis");
        //s生成带有司机标识的的key
        String key = RedisPrefixUtils.generatorKeyByPhone(verificationCodeDTO.getDriverPhone(), IdentityConstants.DRIVER_IDENTITY);

        redisTemplate.opsForValue().set(key,numbercode,1, TimeUnit.MINUTES);

        return ResponseResult.success(numbercode);
    }


    //检查验证码是否正确，并颁发token
    public ResponseResult CheckVerification(String phonenumber, String numbercode) {
        //检查redis中手机号和验证码是否正确
        //获取redis中的验证码
        String number = null;
        String key = RedisPrefixUtils.generatorKeyByPhone(phonenumber,IdentityConstants.DRIVER_IDENTITY);
        number = (String) redisTemplate.opsForValue().get(key);
        if (number!=null&&number.equals(numbercode)){
            System.out.println("检查完毕,结果正确,颁发token");
            //调用自己写的方法得到token
            System.out.println(phonenumber);
            String token = JWTUtils.createJWT(phonenumber.toString(), IdentityConstants.DRIVER_IDENTITY);
            //得到token，把token存入redis设置每一次登录设置时间为30天
            redisTemplate.opsForValue().set(RedisPrefixUtils.gennerateTokenKey(phonenumber,IdentityConstants.DRIVER_IDENTITY),
                    token,30,TimeUnit.DAYS);

            return ResponseResult.success(new TokenResponse(token));
        }else {
            System.out.println("验证码错误");
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR);
        }
    }
}
