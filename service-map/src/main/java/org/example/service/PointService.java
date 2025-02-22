package org.example.service;


import org.example.dto.ResponseResult;
import org.example.remote.PonitClient;
import org.example.request.PointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    @Autowired
    PonitClient ponitClient;

    public ResponseResult upload(PointRequest pointRequest) {
        return ponitClient.upload(pointRequest);
    }
}
