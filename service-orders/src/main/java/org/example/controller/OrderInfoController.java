package org.example.controller;


import org.aspectj.weaver.ast.Or;
import org.example.dto.OrderInfo;
import org.example.dto.ResponseResult;
import org.example.dto.TokenResult;
import org.example.request.DriverGrabRequest;
import org.example.request.OrderRequest;
import org.example.service.GrabService;
import org.example.service.OrderService;
import org.example.utils.JWTUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestController
@RefreshScope
public class OrderInfoController {

    @Autowired
    OrderService orderService;

    /**
     *  用户下订单
     * @param orderRequest
     * @return
     */
    @PostMapping("/order/add")
    public ResponseResult addorder(@RequestBody OrderRequest orderRequest) {

        return orderService.addorder(orderRequest);
    }

    /**
     * 司机出发去乘客点修改订单状态
     * @param orderRequest
     * @return
     */
    @PostMapping("/order/topick-up-passenger")
    public ResponseResult toPickUpPassenger(@RequestBody OrderRequest orderRequest){

        return orderService.toPickUpPassenger(orderRequest);
    }

    /**
     * 司机到达乘客出发地
     * @param orderRequest
     * @return
     */
    @PostMapping("/order/arriveDeparture")
    public ResponseResult ArriveDeparture(@RequestBody OrderRequest orderRequest){
        return orderService.ArriveDepature(orderRequest);
    }

    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/order/pick-up-passenger")
    public ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest){

        return orderService.pickUpPassenger(orderRequest);
    }

    /**
     * 乘客下车，行程终止
     * @param orderRequest
     * @return
     */
    @PostMapping("/order/passenger-getoff")
    public ResponseResult passengerGetOff(@RequestBody OrderRequest orderRequest){

        return orderService.passengerGetOff(orderRequest);
    }


    /**
     * 司机发起收款
     * @param OrderId
     * @return
     */
    @PostMapping("/order/toStartPay")
    public ResponseResult toStartPay (@RequestParam String OrderId){
        return orderService.toStartPay(OrderId);
    }


    /**
     * 取消订单
     * @param OrderId
     * @param identity
     * @return
     */
    @PostMapping("/order/cancel")
    public ResponseResult cancel(Long OrderId,String identity){
          return   orderService.cancel(OrderId,identity);
    }

    /**
     * 乘客支付成功
     * @param orderId
     * @return
     */
    @PostMapping("/order/payFare")
    public ResponseResult payFare(@RequestParam String orderId){
        return orderService.payFare(orderId);
    }

    /**
     * 乘客预定订单，司机抢
     * @param orderRequest
     * @return
     */
    @PostMapping("/order/book")
    ResponseResult bookorder(@RequestBody OrderRequest orderRequest){
        return orderService.book(orderRequest);
    }



    @Autowired
    @Qualifier("GrabServiceByRedisson")
//    @Qualifier("GrabByredisDiy")
//    @Qualifier("GrabByluaRedis")
//    @Qualifier("GrabByzookeeperDiy")
    GrabService grabService;
    /**
     * 司机抢订单
     * @param driverGrabRequest
     * @return
     */
    @PostMapping("/order/grab")
    ResponseResult grab(@RequestBody DriverGrabRequest driverGrabRequest){
        return grabService.grab(driverGrabRequest);
    }
}
