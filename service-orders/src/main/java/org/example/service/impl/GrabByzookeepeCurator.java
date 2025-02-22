package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.checkerframework.checker.units.qual.A;
import org.example.constant.CommonStatusEnum;
import org.example.dto.ResponseResult;
import org.example.request.DriverGrabRequest;
import org.example.service.GrabService;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;


//@Service("GrabByzookeepeCurator")
//@Slf4j
//public class GrabByzookeepeCurator implements GrabService {
//
//    @Autowired
//    OrderService orderService;
//
//    @Autowired
//    CuratorFramework curatorFramework;
//    @Override
//    public ResponseResult grab(DriverGrabRequest driverGrabRequest) {
//        ResponseResult result = null;
//        String key = String.valueOf(driverGrabRequest.getOrderId());
//
//        // 这里 引入不了 InterProcessMatex  还有一个问题，我本地seata启动了，没问题，集成spring有问题
//        //InterProcessMatex  开下语音
//
//
//
//
//        return result;
//    }
//}
