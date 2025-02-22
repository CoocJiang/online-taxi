package org.example.controller;


import org.example.dto.ResponseResult;
import org.example.request.VerificationCodeDTO;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {
    @Autowired
    UserService userService;

    //查询用户是否存在，若不存在就注册，存在就do nothing
    @PostMapping("/user")
    public ResponseResult ReOrLogin(@RequestBody VerificationCodeDTO verificationCodeDTO){
        String phonenumber = verificationCodeDTO.getPassengerPhone();
        return userService.RegOrLogin(phonenumber);
    }


    //查询所有用户
    @GetMapping("/getuser{passengerPhone}")
    public ResponseResult getuser(@PathVariable("passengerPhone") String passengerPhone){
        System.out.println(passengerPhone);
        return userService.getuser(passengerPhone);
    }
}
