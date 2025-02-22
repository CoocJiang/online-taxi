package org.example.remote;


import org.example.dto.ResponseResult;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-map")
public interface MapClient {

    @PostMapping("/addterminal/add")
    public ResponseResult addterminal(@RequestParam(name = "name") String name,
                                      @RequestParam(name = "desc")String desc);


    @PostMapping("/track/add")
    public ResponseResult addtrack(@RequestParam("tid") String tid);
}
