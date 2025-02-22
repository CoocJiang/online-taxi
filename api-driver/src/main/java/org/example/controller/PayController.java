package org.example.controller;


import org.example.dto.ResponseResult;
import org.example.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayController {

    @Autowired
    PayService payService;
    /**
     * 司机发起收款
     * @return
     */
    @PostMapping("/pay/push")
    public ResponseResult pushPayInfo(String orderId ){
        payService.pushPayInfo(orderId);
        return ResponseResult.success();
    }
}
