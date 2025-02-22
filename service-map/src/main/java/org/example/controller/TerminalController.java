package org.example.controller;


import org.example.dto.ResponseResult;
import org.example.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TerminalController {


    @Autowired
    TerminalService terminalService;

    //上传增加终端
    @PostMapping("/addterminal/add")
    public ResponseResult addterminal(@RequestParam String name,@RequestParam String desc){
        return terminalService.addterminal(name,desc);
    }


    //查询周围终端(司机)
    @PostMapping("/aroundsearch")
    public ResponseResult aroundSearch(@RequestParam(name = "center") String center,@RequestParam(name = "radius") String radius){
        return terminalService.aroundSearch(center,radius);
    }


    @PostMapping("/trsearch")
    public ResponseResult trsearch(@RequestParam String tid,@RequestParam Long starttime ,@RequestParam Long endtime ){
        return  terminalService.trsearch(tid,starttime,endtime);
    }
}
