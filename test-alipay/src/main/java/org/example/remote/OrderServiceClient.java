package org.example.remote;


import org.example.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-order")
public interface OrderServiceClient {


    /**
     * 乘客支付成功
     * @param orderId
     * @return
     */
    @PostMapping("/order/payFare")
    public ResponseResult payFare(@RequestParam("orderId") String orderId);
}
