package org.example.controller;


import org.example.dto.ResponseResult;
import org.example.remote.ServicePassengerUserClient;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/users")
    public ResponseResult getuser(HttpServletRequest request){
        String stoken =  request.getHeader("Authorization");
        System.out.println("header"+stoken);
        return userService.getUsertoken(stoken);
    }
}
