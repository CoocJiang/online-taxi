package org.example.service;


import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.example.dto.Car;
import org.example.dto.ResponseResult;
import org.example.remote.AmapServiceClient;
import org.example.remote.ServiceDriverClient;
import org.example.request.ApiDriverPointRequest;
import org.example.request.PointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    @Autowired
    ServiceDriverClient serviceDriverClient;

    @Autowired
    AmapServiceClient amapServiceClient;

    public ResponseResult upload(ApiDriverPointRequest apiDriverPointRequest){
        ResponseResult<Car> responseResult = serviceDriverClient.gatCarbyId(apiDriverPointRequest.getCarId());
        JSONObject jsonObject = JSONObject.fromObject(responseResult.getData());
        String tid = jsonObject.getString("tid");
        String trid = jsonObject.getString("trid");
        PointRequest pointRequest  = new PointRequest(tid,trid,apiDriverPointRequest.getPoints());
        return amapServiceClient.upload(pointRequest);
    }

}
