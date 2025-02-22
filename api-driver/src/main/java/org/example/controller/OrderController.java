package org.example.controller;


import io.seata.spring.annotation.GlobalTransactional;
import org.example.constant.IdentityConstants;
import org.example.dto.ResponseResult;
import org.example.dto.TokenResult;
import org.example.remote.OrderServiceClient;
import org.example.request.OrderRequest;
import org.example.service.OrderService;
import org.example.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class OrderController {

    @Autowired
    OrderServiceClient orderServiceClient;


    @Autowired
    OrderService orderService;
    /**
     * 司机出发去乘客点修改订单状态
     * @param orderRequest
     * @return
     */
    @PostMapping("/order/topick-up-passenger")
    public ResponseResult toPickUpPassenger(@RequestBody OrderRequest orderRequest){
        return orderServiceClient.toPickUpPassenger(orderRequest);
    }

    /**
     * 司机到大乘客出发地
     * @param orderRequest
     * @return
     */
    @PostMapping("/order/arriveDeparture")
    public ResponseResult ArriveDeparture(@RequestBody OrderRequest orderRequest){
        return orderServiceClient.ArriveDeparture(orderRequest);
    }

    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/order/pick-up-passenger")
    public ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest){

        return orderServiceClient.pickUpPassenger(orderRequest);
    }

    /**
     * 到达目的地
     * @param orderRequest
     * @return
     */
    @PostMapping("/order/passenger-getoff")
    public ResponseResult passengerGetOff(@RequestBody OrderRequest orderRequest){

        return orderServiceClient.passengerGetOff(orderRequest);
    }


    /**
     * 司机取消订单
     * @param OrderId
     * @return
     */
    @PostMapping("/order/cancel")
    public ResponseResult cancel(@RequestParam Long OrderId){
        return  orderServiceClient.cancel(OrderId, IdentityConstants.DRIVER_IDENTITY);
    }

    /**
     * 司机发起收款
     * @param OrderId
     * @return
     */
    @PostMapping("/order/toStartPay")
    public ResponseResult toStartPay(@RequestParam("OrderId") String OrderId){
        return orderServiceClient.toStartPay(OrderId);
    }


    /**司机抢单
     * @param OrderId
     * @param request
     * @param receiveOrderCarLatitude
     * @param receiveOrderCarLongitude
     * @return
     */
    @PostMapping("/order/grab")
    ResponseResult grab(@RequestParam("OrderId") String OrderId, HttpServletRequest request,
                        String receiveOrderCarLatitude, String receiveOrderCarLongitude){
        //根据token获取司机信息
        String authorization = request.getHeader("Authorization");

        TokenResult tokenResult = JWTUtils.parseToken(authorization);

        String phone = tokenResult.getPhone();

        return orderService.grab(OrderId,phone,receiveOrderCarLongitude,receiveOrderCarLatitude);
    }
}
