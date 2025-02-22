package org.example.controller;


import org.example.dto.ResponseResult;
import org.example.mapper.DicDistrictMapper;
import org.example.remote.MapDIstrictClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testmapController {
    @Autowired
    DicDistrictMapper dicDistrictMapper;

    @Autowired
    MapDIstrictClient mapDIstrictClient;
    @GetMapping("/testmap")
    public ResponseResult testmap(){
     return mapDIstrictClient.initDicDistrict();
    }
}
