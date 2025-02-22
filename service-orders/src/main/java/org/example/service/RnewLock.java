package org.example.service;


import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Async
public class RnewLock {

    @Autowired
    RedisTemplate redisTemplate;

    //自定义函数实现setnx 给锁续费时间
    public  void RenewRedisLock(String key,String value,int timePerid){

        String s = (String) redisTemplate.opsForValue().get(key);

        if (StringUtil.isNotBlank(s)&&s.equals(value)){
            int renewTime = timePerid/3;
            try {
                TimeUnit.SECONDS.sleep(renewTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //给锁续费时间，其实就是给key续费时间
            redisTemplate.expire(key,timePerid,TimeUnit.SECONDS);
        }else {
            return;
        }
        RenewRedisLock(key,value,timePerid);
    }
}
