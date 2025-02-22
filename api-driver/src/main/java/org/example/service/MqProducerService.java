//package org.example.service;
//
//
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Service
//public class MqProducerService {
//
//    @Autowired
//    RocketMQTemplate rocketMQTemplate;
//
//
//    @PostMapping("/Syncsendmessage")
//    public String SyncSendmessage(String message){
//
//        rocketMQTemplate.syncSend("SpringBoot",message);
//        return "发送成功";
//    }
//}
