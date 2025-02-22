package org.example.controller;


import org.example.dto.ResponseResult;
import org.example.service.ApiServiceFromService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiServiceController {

    @Autowired
    ApiServiceFromService apiServiceFromService;

    //添加服务
    @PostMapping("/service/add/{name}")
    public ResponseResult addservice(@PathVariable String name){

        return apiServiceFromService.addservice(name);
    }
}
