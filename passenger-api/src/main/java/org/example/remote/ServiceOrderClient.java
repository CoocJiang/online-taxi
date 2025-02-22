package org.example.remote;


import org.example.dto.OrderInfo;
import org.example.dto.ResponseResult;
import org.example.request.OrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("service-order")
public interface ServiceOrderClient {
    @PostMapping("/order/add")
    public ResponseResult addorder(@RequestBody OrderRequest orderRequest);

    //为了测试多个乘客同时下单的并发问题
    @GetMapping("/test/order/{orderId}")
    public ResponseResult dispacthRealTimeorder(@PathVariable("orderId") long orderId);


    /**
     * 用户取消订单
     * @param OrderId
     * @param identity
     * @return
     */
    @PostMapping("/order/cancel")
    public ResponseResult cancel(@RequestParam("OrderId") Long OrderId, @RequestParam("identity")String identity);


    @PostMapping("/order/book")
    ResponseResult bookorder(@RequestBody OrderRequest orderRequest);
}
