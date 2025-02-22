package org.example.controller;


import org.example.dto.ResponseResult;
import org.example.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrackController {

    @Autowired
    TrackService trackService;

    @PostMapping("/track/add")
    public ResponseResult addtrack(@RequestParam("tid") String tid){
        return trackService.addtrack(tid);
    }


}
