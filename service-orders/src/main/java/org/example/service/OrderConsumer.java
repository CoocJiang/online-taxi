//package org.example.service;
//
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.example.request.OrderRequest;
//import org.springframework.stereotype.Service;
//
//@Service
//@RocketMQMessageListener(topic = "order-topic", consumerGroup = "order-consumer-group")
//public class OrderConsumer implements RocketMQListener<OrderRequest> {
//
//    @Override
//    public void onMessage(OrderRequest orderInfo) {
//        // 在这里处理订单逻辑，调用 dispatchRealTimeOrder 方法
//        dispatchRealTimeOrder(orderInfo);
//    }
//
//    // 实际处理订单逻辑的方法
//    public void dispatchRealTimeOrder(OrderRequest orderInfo) {
//        System.out.println(orderInfo.getDepLongitude());
//        System.out.println("收到消息");
//        // 处理订单逻辑
//    }
//}
//
