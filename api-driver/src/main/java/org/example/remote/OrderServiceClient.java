package org.example.remote;


import org.example.dto.ResponseResult;
import org.example.request.DriverGrabRequest;
import org.example.request.OrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-order")
public interface OrderServiceClient {
    /**
     * 司机出发去乘客点修改订单状态
     * @param orderRequest
     * @return
     */
    @PostMapping("/order/topick-up-passenger")
    public ResponseResult toPickUpPassenger(@RequestBody OrderRequest orderRequest);

    /**
     * 司机到达乘客出发地
     * @param orderRequest
     * @return
     */
    @PostMapping("/order/arriveDeparture")
    public ResponseResult ArriveDeparture(@RequestBody OrderRequest orderRequest);

    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/order/pick-up-passenger")
    public ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest);

    /**
     * 乘客下车，行程终止
     * @param orderRequest
     * @return
     */
    @PostMapping("/order/passenger-getoff")
    public ResponseResult passengerGetOff(@RequestBody OrderRequest orderRequest);


    /**
     * 司机发起收款
     * @param OrderId
     * @return
     */
    @PostMapping("/order/toStartPay")
    public ResponseResult toStartPay(@RequestParam("OrderId") String OrderId);

    /**
     * 取消订单
     * @param OrderId
     * @param identity
     * @return
     */
    @PostMapping("/order/cancel")
    public ResponseResult cancel(@RequestParam("OrderId") Long OrderId,@RequestParam("identity")String identity);


    /**
     * 司机抢订单
     * @param driverGrabRequest
     * @return
     */
    @PostMapping("/order/grab")
    ResponseResult grab(@RequestBody DriverGrabRequest driverGrabRequest);
}
