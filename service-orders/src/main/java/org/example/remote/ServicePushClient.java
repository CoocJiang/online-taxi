package org.example.remote;


import org.example.utils.SseUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@FeignClient(value = "service-client")
public interface ServicePushClient {

    @GetMapping("/push")
    public String push
            (@RequestParam(value = "UserId") String UserId,
             @RequestParam (value = "identity") String identity,
             @RequestParam (value = "content") String content);


    @GetMapping("/pushprice")
    public String pushprice (@RequestParam(value = "UserId") String UserId,
                             @RequestParam (value = "identity") String identity,
                             @RequestParam (value = "price") String price,
                             @RequestParam (value = "OrderId") String OrderId) ;;
}
