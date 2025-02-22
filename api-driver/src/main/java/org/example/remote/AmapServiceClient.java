package org.example.remote;


import org.example.dto.ResponseResult;
import org.example.request.PointRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("service-map")
public interface AmapServiceClient {

    @PostMapping("/point/upload")
    public ResponseResult upload(@RequestBody PointRequest pointRequest);
}
