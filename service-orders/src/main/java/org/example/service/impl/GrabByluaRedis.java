package org.example.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.example.constant.CommonStatusEnum;
import org.example.dto.ResponseResult;
import org.example.request.DriverGrabRequest;
import org.example.service.GrabService;
import org.example.service.OrderService;
import org.example.service.RnewLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service("GrabByluaRedis")
@Slf4j
public class GrabByluaRedis implements GrabService {
    @Autowired
    OrderService orderService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RnewLock renewlock;

    //spring提供的通过lua脚本操控redis的类
    @Autowired
    DefaultRedisScript<Boolean> redissetScript;
    @Override
    public ResponseResult grab(DriverGrabRequest driverGrabRequest) {
        ResponseResult result = null;
        String key = String.valueOf(driverGrabRequest.getOrderId());
        //分布式中尽量让value不同，自己的服务释放自己的锁
        String value  = String.valueOf(driverGrabRequest.getDriverId())+ UUID.randomUUID();
        List<String> Strings = Arrays.asList(key, value);
        Boolean flag = redisTemplate.execute(redissetScript,Strings,"30");
        if (flag){
            log.info("diy分布式锁开启");
            //自己实现的开启一个线程刷新分布式锁key的时间
            renewlock.RenewRedisLock(key,value,20);
            try {
                TimeUnit.SECONDS.sleep(10);
                result = orderService.grab(driverGrabRequest);
            }catch (Exception e){
                e.printStackTrace();
            } finally {
                String s = (String) redisTemplate.opsForValue().get(key);
                //自己的服务释放自己的锁
                if (s.equals(value)){
                    redisTemplate.delete(key);
                }
                log.info("diy分布式锁解锁成功");
            }
        }else {
            ResponseResult.fail(CommonStatusEnum.ORDER_GRABING.getCode(),CommonStatusEnum.ORDER_GRABING.getValue());
        }
        if (result==null){
            return ResponseResult.fail(CommonStatusEnum.FAIL.getCode(),"抢单失败");
        }else {
            return result;
        }
    }
}
