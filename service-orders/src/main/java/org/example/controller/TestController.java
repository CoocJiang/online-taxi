package org.example.controller;


import org.example.dto.OrderInfo;
import org.example.dto.ResponseResult;
import org.example.mapper.OrderInfoMapper;
import org.example.request.OrderRequest;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class TestController {

    @Autowired
    OrderService orderService;


    @Autowired
    OrderInfoMapper orderInfoMapper;


    @Value("${server.port}")
    String serverport;


    @GetMapping("/test/order/{orderId}")
    public ResponseResult dispacthRealTimeorder(@PathVariable("orderId") long orderId){
        System.out.println("并发测试：---"+orderId+"端口号"+serverport);
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        orderService.dispatchRealTimeOrder(orderInfo);
        return null;
    }
//    @GetMapping("/test/order")
//    public ResponseResult dispacthRealTimeorder(@RequestBody OrderRequest orderRequest){
//        orderService.testorder(orderRequest);
//       return  null;
//    }

}
