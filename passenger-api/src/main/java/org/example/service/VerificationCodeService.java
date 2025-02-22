package org.example.service;



import net.sf.json.JSONObject;
import org.example.constant.CommonStatusEnum;
import org.example.constant.IdentityConstants;
import org.example.dto.ResponseResult;
import org.example.remote.ServicePassengerUserClient;
import org.example.remote.ServiceVefificationcodeClient;
import org.example.request.VerificationCodeDTO;
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
    ServiceVefificationcodeClient serviceVefificationcodeClient ;
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ServicePassengerUserClient servicePassengerUserClient;
    public ResponseResult<NumberCodeResponse> generatorCode(String Phonenumber){
        ResponseResult<NumberCodeResponse> numbbercode = serviceVefificationcodeClient.numbbercode();
        System.out.println(numbbercode.toString());
        //存入redis
        System.out.println("手机号："+Phonenumber);
        JSONObject responseData = JSONObject.fromObject(numbbercode.getData());
        System.out.println("验证码"+responseData.getString("numbercode"));
        System.out.println("存入redis");
        redisTemplate.opsForValue().set(RedisPrefixUtils.generatorKeyByPhone(Phonenumber,IdentityConstants.PASSENGER_IDENTITY),responseData.getString("numbercode"),3, TimeUnit.MINUTES);
        System.out.println("过期时间为3分钟");
        return numbbercode;
    }

    //检查用户手机号和验证码是否正确
    public ResponseResult CheckVerification(String phonenumber,String numbercode){
        //检查redis中手机号和验证码是否正确
        //获取redis中的验证码
        String number = null;
        String key = RedisPrefixUtils.generatorKeyByPhone(phonenumber,IdentityConstants.PASSENGER_IDENTITY);
        System.out.println("获得的key"+key);
        number = (String) redisTemplate.opsForValue().get(key);

        if (number!=null&&number.equals(numbercode)){
            System.out.println("检查完毕,结果正确");
            //调用用户微服务，如果用户第一次登录会自动注册
            try {
                servicePassengerUserClient.LoginOrRes(new VerificationCodeDTO(numbercode,phonenumber,null));
            }catch (RuntimeException e){
                return ResponseResult.fail(CommonStatusEnum.Call_USER_ADD_ERROR.getCode(),CommonStatusEnum.Call_USER_ADD_ERROR.getValue());
            }
            servicePassengerUserClient.LoginOrRes(new VerificationCodeDTO(numbercode,phonenumber,null));
            System.out.println("检查完毕,颁发token");
            //调用自己写的方法得到token
            System.out.println(phonenumber);
            String token = JWTUtils.createJWT(phonenumber.toString(), IdentityConstants.PASSENGER_IDENTITY);
            //得到token，把token存入redis设置每一次登录设置时间为30天
            redisTemplate.opsForValue().set(RedisPrefixUtils.gennerateTokenKey(phonenumber,
                            IdentityConstants.PASSENGER_IDENTITY), token,30,TimeUnit.DAYS);

            return ResponseResult.success(new TokenResponse(token));
        }else {
            System.out.println("验证码错误");
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR);
        }
    }
}
