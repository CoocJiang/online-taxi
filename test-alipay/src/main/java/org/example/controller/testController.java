package org.example.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {

    @GetMapping("/test")
    public String test(){
        System.out.println("回调成功");
        return "支付成功";
    }
}
