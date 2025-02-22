package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.example.constant.CommonStatusEnum;
import org.example.dto.ResponseResult;
import org.example.request.DriverGrabRequest;
import org.example.service.GrabService;
import org.example.service.OrderService;
import org.example.service.RnewLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


//@Service("GrabByzookeeperDiy")
//@Slf4j
//public class GrabByzookeeperDiy implements GrabService {
//
//    @Autowired
//    OrderService orderService;
//
//    @Value("${zookeeper.address}")
//    String address;
//
//    @Value("${zookeeper.timeout}")
//    int timeout;
//
//    @Override
//    public ResponseResult grab(DriverGrabRequest driverGrabRequest) {
//        ResponseResult result = null;
//        String key = String.valueOf(driverGrabRequest.getOrderId());
//        ZooKeeper zooKeeper = null;
//        //这里使用countDownlatch 来做连接成功的判断我们才继续
//        CountDownLatch countDownLatch = new CountDownLatch(1);
//        try {
//             zooKeeper = new ZooKeeper(address, timeout, new Watcher() {
//                @Override
//                public void process(WatchedEvent watchedEvent) {
//                    countDownLatch.countDown();
//                }
//            });
//            countDownLatch.await();
//            //创建持久节点来方便创建临时节点作为分布式锁
//            String parentnode = "/OrderId-"+key;
//            Stat exists = zooKeeper.exists(parentnode, false);
//            if (exists==null){
//                //如果是空的代表不存在
//                                  //节点        节点数据       权限设置                      节点类型
//                zooKeeper.create(parentnode,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//            }
//            //创建临时有序节点
//            String child = zooKeeper.create(parentnode+"/seq","".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
//            //获取zookeeper指定orderid节点下的所有节点
//            List<String> children = zooKeeper.getChildren(parentnode, false);
//            //排序
//            Collections.sort(children);
//            //判断我现在创建的是否是第一个
//            if (!children.isEmpty()&&(parentnode+"/"+children.get(0)).equals(child)){
//                log.info("diy分布式锁开启");
//                result = orderService.grab(driverGrabRequest);
//                log.info("diy分布式锁解锁成功");
//                zooKeeper.close();
//            }else {
//                return ResponseResult.fail(CommonStatusEnum.ORDER_GRABING.getCode(),CommonStatusEnum.ORDER_GRABING.getValue());
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//
//        }
//        return result;
//    }
//}
