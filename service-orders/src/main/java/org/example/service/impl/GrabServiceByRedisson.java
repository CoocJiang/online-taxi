package org.example.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.example.constant.CommonStatusEnum;
import org.example.dto.ResponseResult;
import org.example.request.DriverGrabRequest;
import org.example.service.GrabService;
import org.example.service.OrderService;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("GrabServiceByRedisson")
@Slf4j
//使用redisson实现分布式锁
public class GrabServiceByRedisson implements GrabService {

    @Autowired
    OrderService orderService;

    @Autowired
    RedissonClient redissonClient;

    @Override
    public ResponseResult grab(DriverGrabRequest driverGrabRequest) {
        String orderId = String.valueOf(driverGrabRequest.getOrderId());
        String key = orderId;
        RLock lock = redissonClient.getLock(orderId);
        lock.lock();
        log.info("redisson分布式锁开启");
        ResponseResult result = null;
        try {
            result = orderService.grab(driverGrabRequest);
        }catch (RuntimeException e){
            e.printStackTrace();
        }finally {
            if(lock.isLocked()&&lock.isHeldByCurrentThread()){
                lock.unlock();
                log.info("redisson分布式锁解锁成功");
            }else {
                log.info("error--------");
            }
        }
        if (result==null){
            return ResponseResult.fail(CommonStatusEnum.FAIL.getCode(),"抢单失败");
        }else {
            return result;
        }
    }
}
