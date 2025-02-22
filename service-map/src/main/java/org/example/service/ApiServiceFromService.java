package org.example.service;


import com.google.protobuf.Api;
import org.example.dto.ResponseResult;
import org.example.remote.ApiServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiServiceFromService {


    @Autowired
    ApiServiceClient apiServiceClient;

    public ResponseResult addservice(String name){

        return apiServiceClient.addservice(name);
    }
}
