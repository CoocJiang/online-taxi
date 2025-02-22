package org.example.service;


import org.example.dto.ResponseResult;
import org.example.remote.MapDirectionClient;
import org.example.response.DirectionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DirectionService {
    @Autowired
    MapDirectionClient mapDirectionClient;

    public ResponseResult driving(String depLongitude,String depLatitude,String destLongitude,String destLatitude ) {
        DirectionResponse directionResponse = new DirectionResponse();
        return  ResponseResult.success(mapDirectionClient.direction(depLongitude,depLatitude,destLongitude,destLatitude));
    }
}
