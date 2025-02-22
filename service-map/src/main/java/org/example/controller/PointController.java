package org.example.controller;


import org.example.dto.ResponseResult;
import org.example.request.PointRequest;
import org.example.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointController {


    @Autowired
    PointService pointService;


    /**
     * 司机上传自己的位置
     * @param pointRequest
     * @return
     */
    @PostMapping("/point/upload")
    public ResponseResult upload(@RequestBody PointRequest pointRequest){

        return pointService.upload(pointRequest);
    }

}
