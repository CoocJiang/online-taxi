package org.example.controller;


import org.example.dto.ResponseResult;
import org.example.request.ApiDriverPointRequest;
import org.example.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class PointController {

    @Resource
    PointService pointService;


    @PostMapping("/upload")
    public  ResponseResult upload(@RequestBody ApiDriverPointRequest apiDriverPointRequest){
        return pointService.upload(apiDriverPointRequest);
    }
}
