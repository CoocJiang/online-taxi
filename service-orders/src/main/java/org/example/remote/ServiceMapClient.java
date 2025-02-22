package org.example.remote;


import org.example.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-map")
public interface ServiceMapClient {

    //查询周围可用司机
    @PostMapping("/aroundsearch")
    public ResponseResult aroundSearch(@RequestParam(name = "center") String center,@RequestParam(name = "radius") String radius);


}
