package org.example.controller;



import org.example.dto.ResponseResult;
import org.example.remote.ServiceOrderClient;
import org.example.request.OrderRequest;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@RestController
@Validated
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    ServiceOrderClient serviceOrderClient;

    /**用户下订单
     * @param orderRequest
     * @param request
     * @return
     */
    @PostMapping("/order/add")
    public ResponseResult addorder(@RequestBody OrderRequest orderRequest, HttpServletRequest request){
        return orderService.addorder(orderRequest,request);
    }

    /**
     *
     * @param orderRequest
     * @param request
     * @return
     */
    @PostMapping("/order/book")
    public ResponseResult bookorder(@RequestBody OrderRequest orderRequest, HttpServletRequest request){
        return orderService.bookorder(orderRequest,request);
    }

    //为了测试多个乘客同时下单的并发问题
    @GetMapping("/test/order/{orderId}")
    public ResponseResult dispacthRealTimeorder(@PathVariable("orderId") long orderId){
        System.out.println(orderId);
       return serviceOrderClient.dispacthRealTimeorder(orderId);
    }


    /**
     * 取消订单
     * @param OrderId
     * @return
     */
    @PostMapping("/order/cancel")
    public ResponseResult cancel(@RequestParam @NotNull Long OrderId){
        return  orderService.cancel(OrderId);
    }

    /**
     * 查询订单信息，后面没有调用服务，待完善
     * @param OrderId
     * @return
     */
    @GetMapping("/order/detail")
    public ResponseResult detail(@RequestParam @NotNull Long OrderId){
        return orderService.OrderDetail(OrderId);
    }
}
